-- =========================
-- TABLE: publishers -  Lưu thông tin về nhà xuất bản.
-- =========================
CREATE TABLE publishers
(
    publisher_id   SERIAL PRIMARY KEY,
    publisher_name VARCHAR(100) NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABLE: categories - Lưu thông tin về thể loại sách.
-- =========================
CREATE TABLE categories
(
    category_id   SERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL,
    description   TEXT,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng shelves – Quản lý kệ sách
CREATE TABLE shelves
(
    shelf_id   SERIAL PRIMARY KEY,
    floor      INT         NOT NULL, -- tầng
    code       VARCHAR(20) NOT NULL, -- mã kệ (A1, A2…)
    capacity   INT         NOT NULL, -- tổng số slot trong kệ
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng shelf_slots – Quản lý từng ô/ngăn trong kệ
CREATE TABLE shelf_slots
(
    slot_id     SERIAL PRIMARY KEY,
    shelf_id    INT NOT NULL REFERENCES shelves (shelf_id) ON DELETE CASCADE,
    position    INT NOT NULL,            -- số thứ tự ngăn (1,2,3…)
    is_occupied BOOLEAN   DEFAULT FALSE, -- TRUE nếu đã có sách
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng category_shelving – Mapping thể loại → kệ
CREATE TABLE category_shelving
(
    category_id INT NOT NULL REFERENCES categories (category_id) ON DELETE CASCADE,
    shelf_id    INT NOT NULL REFERENCES shelves (shelf_id) ON DELETE CASCADE,
    PRIMARY KEY (category_id, shelf_id)
);

-- =========================
-- TABLE: books - Lưu thông tin chung về một đầu sách.
-- =========================
CREATE TABLE books
(
    book_id          SERIAL PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    isbn             VARCHAR(20) UNIQUE,
    author           VARCHAR(100) NOT NULL,
    publication_year INT,
    description      TEXT,
    publisher_id     INT          REFERENCES publishers (publisher_id) ON DELETE SET NULL,
    category_id      INT          REFERENCES categories (category_id) ON DELETE SET NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABLE: book_copies -  Lưu thông tin về từng bản sao vật lý của một đầu sách.
-- =========================
CREATE TABLE book_copies
(
    copy_id           SERIAL PRIMARY KEY,
    book_id           INT                NOT NULL REFERENCES books (book_id) ON DELETE CASCADE,
    barcode           VARCHAR(50) UNIQUE NOT NULL,
    barcode_image_url TEXT,
    status            VARCHAR(20)        NOT NULL DEFAULT 'Available'
        CHECK (status IN ('Available', 'Loaned', 'Lost', 'Damaged', 'Reserved')),
    slot_id           INT REFERENCES shelf_slots (slot_id), -- vị trí sách trên kệ
    created_at        TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABLE: members - Lưu thông tin về bạn đọc/thành viên của thư viện.
-- =========================
CREATE TABLE members
(
    member_id         SERIAL PRIMARY KEY,
    full_name         VARCHAR(100)       NOT NULL,
    email             VARCHAR(100) UNIQUE,
    phone_number      VARCHAR(20),
    address           VARCHAR(255),
    card_number       VARCHAR(50) UNIQUE NOT NULL,
    registration_date DATE               NOT NULL,
    expiry_date       DATE               NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABLE: loans - Ghi lại các giao dịch mượn sách.
-- =========================
CREATE TABLE loans
(
    loan_id     SERIAL PRIMARY KEY,
    copy_id     INT         NOT NULL REFERENCES book_copies (copy_id) ON DELETE CASCADE,
    member_id   INT         NOT NULL REFERENCES members (member_id) ON DELETE CASCADE,
    loan_date   DATE        NOT NULL,
    due_date    DATE        NOT NULL,
    return_date DATE,
    status      VARCHAR(20) NOT NULL DEFAULT 'Active'
        CHECK (status IN ('Active', 'Returned', 'Overdue')),
    created_at  TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP            DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABLE: reservations -  Ghi lại các yêu cầu đặt trước sách.
-- =========================
CREATE TABLE reservations
(
    reservation_id   SERIAL PRIMARY KEY,
    book_id          INT         NOT NULL REFERENCES books (book_id) ON DELETE CASCADE,
    member_id        INT         NOT NULL REFERENCES members (member_id) ON DELETE CASCADE,
    reservation_date TIMESTAMP   NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'Pending'
        CHECK (status IN ('Pending', 'Fulfilled', 'Expired', 'Canceled')),
    created_at       TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP            DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABLE: fines -  Ghi lại các khoản phí phạt liên quan đến các vi phạm.
-- =========================
CREATE TABLE fines
(
    fine_id     SERIAL PRIMARY KEY,
    loan_id     INT            NOT NULL REFERENCES loans (loan_id) ON DELETE CASCADE,
    member_id   INT            NOT NULL REFERENCES members (member_id) ON DELETE CASCADE,
    fine_amount DECIMAL(10, 2) NOT NULL,
    paid_amount DECIMAL(10, 2)          DEFAULT 0.00,
    fine_date   DATE           NOT NULL,
    status      VARCHAR(10)    NOT NULL DEFAULT 'Unpaid'
        CHECK (status IN ('Unpaid', 'Paid')),
    created_at  TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP               DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABLE: roles -  Định nghĩa các vai trò trong hệ thống.
-- =========================
CREATE TABLE roles
(
    role_id     SERIAL PRIMARY KEY,
    role_name   VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABLE: users - Lưu thông tin tài khoản đăng nhập hệ thống của nhân viên (Thủ thư, Admin).
-- =========================
CREATE TABLE users
(
    user_id       SERIAL PRIMARY KEY,
    role_id       INT                REFERENCES roles (role_id) ON DELETE SET NULL,
    username      VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255)       NOT NULL,
    full_name     VARCHAR(100),
    email         VARCHAR(100) UNIQUE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
