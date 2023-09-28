package de.bacherik.bansystem.utils.config;

import de.bacherik.bansystem.utils.ServerSoftware;

import java.util.LinkedHashMap;

public abstract class MessagesConfig extends Config {

    protected LinkedHashMap<String, String> messages;

    public MessagesConfig(String name, String path, ServerSoftware serverSoftware) {
        super(name, path, serverSoftware);

        this.messages = new LinkedHashMap<>();
        loadMessages();
        messages.forEach((messagePath, message) -> messages.put(messagePath, (String) getPathOrSet(messagePath, message)));
    }

    public abstract void loadMessages();

    public String get(String path) {
        return get(path, false);
    }

    public String get(String path, boolean prefix) {
        return ((prefix ? messages.get("bansystem.prefix") : "") + messages.get(path));
    }

    public void reload() {
        super.reload();

        messages.forEach((messagePath, message) -> messages.put(messagePath, (String) getPathOrSet(messagePath, message)));
    }
}
