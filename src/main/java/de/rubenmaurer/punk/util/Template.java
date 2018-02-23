package de.rubenmaurer.punk.util;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

/**
 * Helper for accessing string templates.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class Template {

    /**
     * Loaded template
     */
    private ST template;

    /**
     * Group of available templates.
     */
    private static STGroup templates;

    /**
     * Constructor for a new template.
     *
     * @param template the template to load
     */
    private Template(ST template) {
        this.template = template;
    }

    /**
     * Load a specific template from template pool.
     *
     * @param template the template to load
     * @return the loaded template
     */
    public static Template get(String template) {
        if (templates == null) templates = new STGroupFile("templates/messages.stg");
        return new Template(templates.getInstanceOf(template));
    }

    /**
     * Fill a single var in loaded template.
     *
     * @param key the key
     * @param value the value
     * @return the rendered template
     */
    public String single(String key, String value) {
        return template.add(key, value).render();
    }

    /**
     * Fill multiple vars in loaded template.
     *
     * @param keys the keys
     * @param values the values
     * @return the rendered template
     */
    public String multiple(String[] keys, String[] values) {
        if (values.length != keys.length) throw new IllegalArgumentException("ERROR");

        for (int i = 0; i < keys.length; i++) {
            template.add(keys[i], values[i]);
        }

        return template.render();
    }

    /**
     * Render a template without replaced vars.
     *
     * @return the rendered template
     */
    @Override
    public String toString() {
        return template.render();
    }
}
