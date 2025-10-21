-- Report system migration for Engagement Service
-- This migration creates tables for reporting novels and comments

-- Report types enum
DO $$ BEGIN
    CREATE TYPE report_type AS ENUM (
        'PORNOGRAPHIC',
        'HATE_BULLYING',
        'PERSONAL_INFO',
        'INAPPROPRIATE',
        'SPAM'
    );
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

-- Report status enum
DO $$ BEGIN
    CREATE TYPE report_status AS ENUM (
        'IN_REVIEW',
        'RESOLVED',
        'DISMISSED'
    );
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

-- Main report table
CREATE TABLE IF NOT EXISTS report (
    id SERIAL PRIMARY KEY,
    uuid UUID NOT NULL DEFAULT gen_random_uuid(),
    reporter_id UUID NOT NULL,
    report_type report_type NOT NULL,
    reason TEXT NOT NULL,
    status report_status DEFAULT 'IN_REVIEW',
    admin_notes TEXT,
    resolved_by UUID,
    content_type VARCHAR(10) NOT NULL, -- 'NOVEL' or 'COMMENT'
    content_id INTEGER NOT NULL, -- ID of novel or comment
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- Note: Foreign key constraints removed as referenced tables don't exist yet
    -- CONSTRAINT fk_report_reporter FOREIGN KEY (reporter_id) REFERENCES users(uuid) ON DELETE CASCADE,
    -- CONSTRAINT fk_report_resolved_by FOREIGN KEY (resolved_by) REFERENCES users(uuid) ON DELETE SET NULL,
    CONSTRAINT chk_content_type CHECK (content_type IN ('NOVEL', 'COMMENT'))
);