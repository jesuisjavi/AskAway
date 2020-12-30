package com.ex.utilities;

import com.ex.models.AwardableActions;

public final class PointsUtil {
    private static final int ADDED = 1;
    private static final int ANSWERED = 1;
    private static final int TOP_ANSWER = 10;

    public static int AwardPoints(AwardableActions action){
        switch(action){
            case ADDED:
                return ADDED;
            case ANSWERED:
                return ANSWERED;
            case TOP_ANSWER:
                return TOP_ANSWER;
            default:
                return 0;
        }
    }
}
