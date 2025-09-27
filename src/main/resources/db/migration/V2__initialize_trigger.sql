-- Function sinh slot
CREATE OR REPLACE FUNCTION create_shelf_slots()
    RETURNS TRIGGER AS
$$
BEGIN
    -- Sinh slot từ 1 -> capacity của kệ vừa thêm
    INSERT INTO shelf_slots (shelf_id, position)
    SELECT NEW.shelf_id, g
    FROM generate_series(1, NEW.capacity) g;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- gọi function khi có kệ mới
CREATE TRIGGER trg_create_shelf_slots
    AFTER INSERT
    ON shelves
    FOR EACH ROW
EXECUTE FUNCTION create_shelf_slots();

-- Function tự động gán slot khi có bản sao sách mới
CREATE OR REPLACE FUNCTION assign_book_to_slot()
    RETURNS TRIGGER AS
$$
DECLARE
    free_slot_id INT;
BEGIN
    -- Tìm 1 slot trống trong kệ được gán với category của sách
    SELECT ss.slot_id
    INTO free_slot_id
    FROM books b
             JOIN category_shelving cs ON cs.category_id = b.category_id
             JOIN shelves s ON s.shelf_id = cs.shelf_id
             JOIN shelf_slots ss ON ss.shelf_id = s.shelf_id
    WHERE b.book_id = NEW.book_id
      AND ss.is_occupied = FALSE
    ORDER BY s.shelf_id, ss.position
    LIMIT 1;

    -- Nếu không có slot trống
    IF free_slot_id IS NULL THEN
        NEW.slot_id := NULL;
    END IF;

    -- Gán slot vào bản sao sách
    NEW.slot_id := free_slot_id;

    -- Đánh dấu slot đã có sách
    UPDATE shelf_slots
    SET is_occupied = TRUE,
        updated_at  = CURRENT_TIMESTAMP
    WHERE slot_id = free_slot_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- trigger gọi function khi có bản sao sách mới
CREATE TRIGGER trg_assign_book_to_slot
    BEFORE INSERT
    ON book_copies
    FOR EACH ROW
EXECUTE FUNCTION assign_book_to_slot();

-- Function giải phóng slot khi xóa sách
CREATE OR REPLACE FUNCTION free_slot_on_book_delete()
    RETURNS TRIGGER AS
$$
BEGIN
    -- Nếu bản sao sách có slot_id thì mở lại slot
    IF OLD.slot_id IS NOT NULL THEN
        UPDATE shelf_slots
        SET is_occupied = FALSE,
            updated_at  = CURRENT_TIMESTAMP
        WHERE slot_id = OLD.slot_id;
    END IF;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- Tigger gọi function khi xóa bản sao sách
CREATE TRIGGER trg_free_slot_on_book_delete
    AFTER DELETE
    ON book_copies
    FOR EACH ROW
EXECUTE FUNCTION free_slot_on_book_delete();

-- Function đồng bộ shelf_slots khi đổi capacity
CREATE OR REPLACE FUNCTION sync_shelf_slots_on_capacity_update()
    RETURNS TRIGGER AS
$$
DECLARE
    current_capacity INT;
    occupied_count   INT;
BEGIN
    -- Đếm số slot hiện tại của kệ
    SELECT COUNT(*)
    INTO current_capacity
    FROM shelf_slots
    WHERE shelf_id = NEW.shelf_id;

    -- Trường hợp capacity mới nhỏ hơn capacity cũ
    IF NEW.capacity < current_capacity THEN
        -- Đếm số slot đang có sách
        SELECT COUNT(*)
        INTO occupied_count
        FROM shelf_slots
        WHERE shelf_id = NEW.shelf_id
          AND is_occupied = TRUE;

        -- Nếu số slot có sách > capacity mới thì báo lỗi
        IF occupied_count > NEW.capacity THEN
            RAISE EXCEPTION 'Không thể giảm capacity xuống %, vì đang có % slot chứa sách!',
                NEW.capacity, occupied_count;
        END IF;

        -- Xóa bớt slot thừa (ưu tiên slot trống, vị trí lớn nhất)
        DELETE
        FROM shelf_slots
        WHERE slot_id IN (SELECT slot_id
                          FROM shelf_slots
                          WHERE shelf_id = NEW.shelf_id
                            AND is_occupied = FALSE
                          ORDER BY position DESC
                          LIMIT (current_capacity - NEW.capacity));
    END IF;

    -- Trường hợp capacity mới lớn hơn capacity cũ → thêm slot mới
    IF NEW.capacity > current_capacity THEN
        INSERT INTO shelf_slots (shelf_id, position)
        SELECT NEW.shelf_id, g
        FROM generate_series(current_capacity + 1, NEW.capacity) g;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger gọi function khi update shelves.capacity
CREATE TRIGGER trg_sync_shelf_slots_on_capacity_update
    AFTER UPDATE OF capacity
    ON shelves
    FOR EACH ROW
EXECUTE FUNCTION sync_shelf_slots_on_capacity_update();
