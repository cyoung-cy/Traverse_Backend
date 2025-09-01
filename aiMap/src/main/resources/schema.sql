CREATE TABLE IF NOT EXISTS quests (
    id CHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    difficulty VARCHAR(50),
    avg_minutes INT,
    need_people VARCHAR(50),
    area_code VARCHAR(10),
    sigungu_code VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);