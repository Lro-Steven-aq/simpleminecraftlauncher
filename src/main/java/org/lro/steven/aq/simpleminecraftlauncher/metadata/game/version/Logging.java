package org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version;

public record Logging(Client client) {


    public record Client(
            String argument,
            File file,
            String type
    ) {
    }
}
