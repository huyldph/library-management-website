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

-- =========================
-- TABLE: books
-- =========================
CREATE TABLE books - Lưu thông tin chung về một đầu sách
(
    tiêu
    đề,
    ISBN,
    năm
    xuất
    bản
    .
    .
    .
).
(
    book_id
    SERIAL
    PRIMARY
    KEY,
    title
    VARCHAR
(
    255
) NOT NULL,
    isbn VARCHAR
(
    20
) UNIQUE,
    publication_year INT,
    description TEXT,
    publisher_id INT REFERENCES publishers
(
    publisher_id
) ON DELETE SET NULL,
    category_id INT REFERENCES categories
(
    category_id
)
  ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- =========================
-- TABLE: authors - Lưu thông tin về tác giả.
-- =========================
CREATE TABLE authors
(
    author_id   SERIAL PRIMARY KEY,
    author_name VARCHAR(100) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABLE: book_authors (many-to-many)
-- =========================
CREATE TABLE book_authors
(
    book_id   INT REFERENCES books (book_id) ON DELETE CASCADE,
    author_id INT REFERENCES authors (author_id) ON DELETE CASCADE,
    PRIMARY KEY (book_id, author_id)
);

-- =========================
-- TABLE: book_copies -  Lưu thông tin về từng bản sao vật lý của một đầu sách.
-- =========================
CREATE TABLE book_copies
(
    copy_id    SERIAL PRIMARY KEY,
    book_id    INT                NOT NULL REFERENCES books (book_id) ON DELETE CASCADE,
    barcode    VARCHAR(50) UNIQUE NOT NULL,
    status     VARCHAR(20)        NOT NULL DEFAULT 'Available'
        CHECK (status IN ('Available', 'Loaned', 'Lost', 'Damaged', 'Reserved')),
    location   VARCHAR(100),
    created_at TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABLE: members
-- =========================
CREATE TABLE members - Lưu thông tin về bạn đọc/thành viên của thư viện.
(
    member_id
    SERIAL
    PRIMARY
    KEY,
    full_name
    VARCHAR
(
    100
) NOT NULL,
    email VARCHAR
(
    100
) UNIQUE,
    phone_number VARCHAR
(
    20
),
    address VARCHAR
(
    255
),
    card_number VARCHAR
(
    50
) UNIQUE NOT NULL,
    registration_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
-- TABLE: users - Lưu thông tin tài khoản đăng nhập hệ thống của nhân viên (Thủ thư, Admin).
-- =========================
CREATE TABLE users
(
    user_id       SERIAL PRIMARY KEY,
    username      VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255)       NOT NULL,
    full_name     VARCHAR(100),
    email         VARCHAR(100) UNIQUE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
-- TABLE: user_roles (many-to-many)
-- =========================
CREATE TABLE user_roles
(
    user_id INT REFERENCES users (user_id) ON DELETE CASCADE,
    role_id INT REFERENCES roles (role_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);
