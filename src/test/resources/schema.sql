-- Test schema for engagement service
-- This file is used for H2 database initialization in test profile

-- Comment table
CREATE TABLE IF NOT EXISTS comment (
    id SERIAL PRIMARY KEY,
    uuid VARCHAR(36) NOT NULL DEFAULT RANDOM_UUID(),
    user_id VARCHAR(36) NOT NULL,
    novel_id INTEGER NOT NULL,
    chapter_id INTEGER,
    parent_id INTEGER,
    content TEXT NOT NULL,
    like_cnt INTEGER DEFAULT 0,
    is_deleted BOOLEAN DEFAULT FALSE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Review table
CREATE TABLE IF NOT EXISTS review (
    id SERIAL PRIMARY KEY,
    uuid VARCHAR(36) NOT NULL DEFAULT RANDOM_UUID(),
    user_id VARCHAR(36) NOT NULL,
    novel_id INTEGER NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    title VARCHAR(255),
    content TEXT,
    like_cnt INTEGER DEFAULT 0,
    is_spoiler BOOLEAN DEFAULT FALSE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_user_novel_review UNIQUE (user_id, novel_id)
);

-- Vote table
CREATE TABLE IF NOT EXISTS vote (
    id SERIAL PRIMARY KEY,
    uuid VARCHAR(36) NOT NULL DEFAULT RANDOM_UUID(),
    user_id VARCHAR(36) NOT NULL,
    novel_id INTEGER NOT NULL,
    vote_type VARCHAR(20) NOT NULL CHECK (vote_type IN ('LIKE', 'DISLIKE')),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_user_novel_vote UNIQUE (user_id, novel_id)
);

-- Report table
CREATE TABLE IF NOT EXISTS report (
    id SERIAL PRIMARY KEY,
    uuid VARCHAR(36) NOT NULL DEFAULT RANDOM_UUID(),
    reporter_id VARCHAR(36) NOT NULL,
    target_type VARCHAR(20) NOT NULL CHECK (target_type IN ('NOVEL', 'COMMENT', 'REVIEW')),
    target_id INTEGER NOT NULL,
    report_type VARCHAR(20) NOT NULL CHECK (report_type IN ('PORNOGRAPHIC', 'HATE_BULLYING', 'PERSONAL_INFO', 'INAPPROPRIATE', 'SPAM')),
    reason TEXT,
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
    admin_id VARCHAR(36),
    admin_note TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
