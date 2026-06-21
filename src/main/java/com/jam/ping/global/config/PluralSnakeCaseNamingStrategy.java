package com.jam.ping.global.config;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class PluralSnakeCaseNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {

    @Override
    public Identifier toPhysicalTableName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
        Identifier snakeCased = super.toPhysicalTableName(logicalName, jdbcEnvironment);
        return Identifier.toIdentifier(pluralize(snakeCased.getText()));
    }

    private String pluralize(String name) {
        if (name.endsWith("y") && name.length() > 1 && !isVowel(name.charAt(name.length() - 2))) {
            return name.substring(0, name.length() - 1) + "ies";
        }
        return name + "s";
    }

    private boolean isVowel(char c) {
        return "aeiouAEIOU".indexOf(c) >= 0;
    }
}
