package org.meldtech.platform.glrest.utl;

import lombok.Getter;

@Getter
public enum GraphQlOps {
    QUERY("query"),
    MUTATION("mutation"),
    SUBSCRIPTION("subscription");

    private final String value;

    GraphQlOps(String value) {
        this.value = value;
    }

}
