package io.github.superjoy0502.tarotbot;

import io.github.superjoy0502.tarotbot.listeners.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class Bot {
    // Terms of Service: https://sagebot.notion.site/Terms-of-Service-5cecb4dae3b34816a8b58e0a93ce1b2e
    // Privacy Policy: https://sagebot.notion.site/Privacy-Policy-479b3b8481044223880d6b3e9e92fd41
    static final String token = System.getenv("DISCORD_TOKEN");

    public static void main(String[] args) {
        JDA api = JDABuilder.createDefault(token).build();
        setupCommands(api);
        setupListeners(api);
    }

    private static void setupCommands(JDA api) {
        api.updateCommands().addCommands(
                Commands.slash("thread", "Create a thread for tarot.")
                        .addSubcommands(
                                new SubcommandData("public", "Create a public thread for tarot."),
                                new SubcommandData("private", "Create a private thread for tarot.")
                        ),
                Commands.slash("ask", "A simple tarot reading.")
                        .addOptions(
                                new OptionData(OptionType.STRING, "question", "The question you want to ask.", true),
                                new OptionData(OptionType.BOOLEAN, "private", "Whether the reading should be private.", false)
                        )
        ).queue();
    }

    private static void setupListeners(JDA api) {
        api.addEventListener(new CommandListener(api));
    }
}
