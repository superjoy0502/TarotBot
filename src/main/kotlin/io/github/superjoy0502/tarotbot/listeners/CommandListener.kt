package io.github.superjoy0502.tarotbot.listeners

import io.github.superjoy0502.tarotbot.Tarot
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.channel.attribute.IThreadContainer
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionMapping

class CommandListener(api: JDA) : ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name == "thread") {
            event.deferReply(true).queue()
            val guild: Guild? = event.guild
            if (guild == null) {
                event.hook.sendMessage("Available in servers only.").queue()
                return
            } else {
                val threadContainer: IThreadContainer = event.channel as IThreadContainer
                val caster: Member = event.member!!
                val subcommand = event.subcommandName
                if (subcommand == "public") {
                    // Create public thread
                    threadContainer.createThreadChannel("${caster.effectiveName}'s Tarot", false).queue { thread ->
                        thread.sendMessage("${caster.asMention} Your thread has been created.").queue()
                    }
                    event.hook.sendMessage("Public thread created.").queue()
                } else if (subcommand == "private") {
                    // Create private thread
                    threadContainer.createThreadChannel("${caster.effectiveName}'s private Tarot", true)
                        .queue { thread ->
                            thread.sendMessage("${caster.asMention} Your thread has been created.").queue()
                        }
                    event.hook.sendMessage("Private thread created.").queue()
                }
            }
        } else if (event.name == "ask") {
            event.deferReply(event.getOption("private", false, OptionMapping::getAsBoolean)).queue()
            val question: String = event.getOption("question", OptionMapping::getAsString).toString()
            val reader: Tarot = Tarot()
            reader.answer(reader, question, event)
        }
    }
}