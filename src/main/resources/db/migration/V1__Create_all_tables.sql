-- Create all tables for Engagement Service
-- This migration creates comment and review tables with all necessary indexes and constraints

-- Comment table
CREATE TABLE IF NOT EXISTS comment (
    id SERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    chapter_id INTEGER NOT NULL,
    content TEXT NOT NULL,
    like_cnt INTEGER DEFAULT 0,
    is_spoiler BOOLEAN DEFAULT FALSE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    -- Note: Foreign key constraints removed as referenced tables don't exist yet
    -- CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users(uuid) ON DELETE CASCADE,
    -- CONSTRAINT fk_comment_chapter FOREIGN KEY (chapter_id) REFERENCES chapter(id) ON DELETE CASCADE
);

-- Review table for novel reviews
CREATE TABLE review (
    id SERIAL PRIMARY KEY,
    uuid UUID NOT NULL,
    user_id UUID NOT NULL,
    novel_id INTEGER NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    title VARCHAR(255),
    content TEXT,
    like_cnt INTEGER DEFAULT 0,
    is_spoiler BOOLEAN DEFAULT FALSE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_user_novel_review UNIQUE (user_id, novel_id)
    -- Note: Foreign key constraints removed as referenced tables don't exist yet
    -- CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(uuid) ON DELETE CASCADE,
    -- CONSTRAINT fk_review_novel FOREIGN KEY (novel_id) REFERENCES novel(id) ON DELETE CASCADE,
);

-- Vote table for novel votes
CREATE TABLE vote (
    id SERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    novel_id INTEGER NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    -- Note: Foreign key constraints removed as referenced tables don't exist yet
    -- CONSTRAINT fk_vote_user FOREIGN KEY (user_id) REFERENCES users(uuid) ON DELETE CASCADE,
    -- CONSTRAINT fk_vote_novel FOREIGN KEY (novel_id) REFERENCES novel(id) ON DELETE CASCADE,
);
