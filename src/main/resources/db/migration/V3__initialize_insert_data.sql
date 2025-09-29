-- =========================
-- INSERT publishers
-- =========================
INSERT INTO publishers (publisher_name)
VALUES ('NXB Kim Đồng'),
       ('NXB Trẻ'),
       ('NXB Giáo Dục'),
       ('NXB Lao Động');

-- =========================
-- INSERT categories
-- =========================
INSERT INTO categories (category_name, description)
VALUES ('Thiếu nhi', 'Sách dành cho thiếu nhi'),
       ('Khoa học', 'Sách nghiên cứu khoa học'),
       ('Văn học', 'Sách văn học Việt Nam và thế giới'),
       ('Kỹ năng sống', 'Sách kỹ năng sống và phát triển bản thân');

-- =========================
-- INSERT shelves
-- =========================
INSERT INTO shelves (floor, code, capacity)
VALUES (1, 'A1', 10),
       (1, 'A2', 8),
       (2, 'B1', 12),
       (2, 'B2', 15);

-- =========================
-- INSERT category_shelving (mapping category → shelf)
-- =========================
INSERT INTO category_shelving (category_id, shelf_id)
VALUES (1, 1), -- Thiếu nhi → A1
       (2, 2), -- Khoa học → A2
       (3, 3), -- Văn học → B1
       (4, 4); -- Kỹ năng sống → B2

-- =========================
-- INSERT books
-- =========================
INSERT INTO books (title, isbn, author, publication_year, description, publisher_id, category_id)
VALUES ('Doraemon tập 1', '9786042081234', 'Fujiko F Fujio', 1992, 'Truyện tranh Nhật Bản nổi tiếng', 1, 1),
       ('Vật lý vui', '9786041234567', 'Yakob Perelman', 2000, 'Sách khoa học phổ thông', 3, 2),
       ('Truyện Kiều', '9786047654321', 'Nguyễn Du', 1820, 'Tác phẩm văn học cổ điển', 2, 3),
       ('Đắc nhân tâm', '9786041111111', 'Dale Carnegie', 1936, 'Sách kỹ năng sống nổi tiếng', 4, 4);

-- =========================
-- INSERT book_copies (mỗi sách vài bản sao gán vào slot)
-- =========================
INSERT INTO book_copies (book_id, barcode, status, slot_id)
VALUES (1, 'BC001', 'Available', 1),
       (1, 'BC002', 'Loaned', 2),
       (2, 'BC003', 'Available', 4),
       (3, 'BC004', 'Available', 7),
       (4, 'BC005', 'Reserved', 10);

-- =========================
-- INSERT members
-- =========================
INSERT INTO members (full_name, email, phone_number, address, card_number, registration_date, expiry_date)
VALUES ('Nguyễn Văn A', 'a@example.com', '0909123456', 'Hà Nội', 'CARD001', '2025-01-01', '2026-01-01'),
       ('Trần Thị B', 'b@example.com', '0909234567', 'Hồ Chí Minh', 'CARD002', '2025-02-01', '2026-02-01'),
       ('Lê Văn C', 'c@example.com', '0909345678', 'Đà Nẵng', 'CARD003', '2025-03-01', '2026-03-01');

-- =========================
-- INSERT loans
-- =========================
INSERT INTO loans (loan_code,copy_id, member_id, loan_date, due_date, return_date, status)
VALUES ('L001',2, 1, '2025-09-01', '2025-09-15', NULL, 'Active'),
       ('L002',3, 2, '2025-08-10', '2025-08-24', '2025-08-22', 'Returned'),
       ('L003',4, 3, '2025-07-05', '2025-07-19', NULL, 'Overdue');

-- =========================
-- INSERT reservations
-- =========================
INSERT INTO reservations (book_id, member_id, reservation_date, status)
VALUES (1, 2, '2025-09-20 10:00:00', 'Pending'),
       (4, 1, '2025-09-18 14:30:00', 'Fulfilled');

-- =========================
-- INSERT fines
-- =========================
INSERT INTO fines (loan_id, member_id, fine_amount, paid_amount, fine_date, status)
VALUES (3, 3, 50000.00, 0.00, '2025-07-25', 'Unpaid'),
       (2, 2, 20000.00, 20000.00, '2025-08-25', 'Paid');
