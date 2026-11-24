package com.jobPortal.careerBridge.enums;

public enum RecruitmentStage {
    SCREENING,
    TECHNICAL_INTERVIEW,
    MANAGER_INTERVIEW,
    HR_INTERVIEW,
    OFFER,
    HIRED,
    REJECTED,
    WITHDRAWN;

    public boolean isTerminal(){
        return this == HIRED
                || this == REJECTED
                || this == WITHDRAWN;
    }

    public boolean canMoveForward(RecruitmentStage newStage){
        return getProgressionOrder(newStage) > getProgressionOrder(this);
    }

    public boolean requiresSkipReason(RecruitmentStage newStage){
        int difference = getProgressionOrder(newStage)
                - getProgressionOrder(this);
        return difference > 1;
    }

    private int getProgressionOrder(RecruitmentStage stage){
        return switch (stage){
            case SCREENING -> 0  ;
            case TECHNICAL_INTERVIEW ->  1;
            case MANAGER_INTERVIEW ->  2;
            case HR_INTERVIEW ->  3;
            case OFFER -> 4;
            case HIRED , REJECTED,WITHDRAWN-> -1;
        };
    }



}
