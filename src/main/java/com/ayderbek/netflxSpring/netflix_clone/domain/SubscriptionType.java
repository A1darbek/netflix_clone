package com.ayderbek.netflxSpring.netflix_clone.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.EnumMap;
import java.util.Map;

public  enum SubscriptionType {
    BASIC, STANDARD, PREMIUM;

    public static class Features {
        static final Map<SubscriptionType, FeatureSet> features = new EnumMap<>(SubscriptionType.class);

        static {
            features.put(BASIC, new FeatureSet(1, false, "SD"));
            features.put(STANDARD, new FeatureSet(2, true, "HD"));
            features.put(PREMIUM, new FeatureSet(4, true, "HD/UHD 4K"));
        }

        public static FeatureSet get(SubscriptionType type) {
            return features.get(type);
        }
    }

    @Data
    @AllArgsConstructor
    public static class FeatureSet {
        private final int maxScreens;
        private final boolean includesHD;
        private final String videoQuality;
    }
}

