package de.rubenmaurer.punk.util;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class Template {
    private ST template;

    private static STGroup templates;

    private Template(ST template) {
        this.template = template;
    }

    public static Template get(String template) {
        if (templates == null) templates = new STGroupFile("templates/messages.stg");
        return new Template(templates.getInstanceOf(template));
    }

    public String single(String key, String value) {
        return template.add(key, value).render();
    }

    public String multiple(String[] keys, String[] values) {
        if (values.length != keys.length) throw new IllegalArgumentException("ERROR");

        for (int i = 0; i < keys.length; i++) {
            template.add(keys[i], values[i]);
        }

        return template.render();
    }

    @Override
    public String toString() {
        return template.render();
    }
}
