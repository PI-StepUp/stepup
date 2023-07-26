package com.pi.stepup.domain.dance.constant;

public enum ProgressType {
    ALL{
        @Override
        public String toString() {
            return "ALL";
        }
    }, SCHEDULED{
        @Override
        public String toString() {
            return "SCHEDULED";
        }
    }, IN_PROGRESS{
        @Override
        public String toString() {
            return "IN_PROGRESS";
        }
    };

}
