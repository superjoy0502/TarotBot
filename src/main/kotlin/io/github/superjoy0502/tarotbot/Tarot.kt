package io.github.superjoy0502.tarotbot

import com.aallam.openai.api.completion.CompletionRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class Tarot {

    val apikey: String = System.getenv("APIKEY")
    val DAVINCI = ModelId("text-davinci-003")
    val CURIE = ModelId("text-curie-001")
    val BABBAGE = ModelId("text-babbage-001")
    val ADA = ModelId("text-ada-001")

    val cards = mapOf(
        0 to "The Fool",
        1 to "The Magician",
        2 to "The High Priestess",
        3 to "The Empress",
        4 to "The Emperor",
        5 to "The Hierophant",
        6 to "The Lovers",
        7 to "The Chariot",
        8 to "Strength",
        9 to "The Hermit",
        10 to "Wheel of Fortune",
        11 to "Justice",
        12 to "The Hanged Man",
        13 to "Death",
        14 to "Temperance",
        15 to "The Devil",
        16 to "The Tower",
        17 to "The Star",
        18 to "The Moon",
        19 to "The Sun",
        20 to "Judgement",
        21 to "The World",
        22 to "Ace of Wands",
        23 to "Two of Wands",
        24 to "Three of Wands",
        25 to "Four of Wands",
        26 to "Five of Wands",
        27 to "Six of Wands",
        28 to "Seven of Wands",
        29 to "Eight of Wands",
        30 to "Nine of Wands",
        31 to "Ten of Wands",
        32 to "Page of Wands",
        33 to "Knight of Wands",
        34 to "Queen of Wands",
        35 to "King of Wands",
        36 to "Ace of Cups",
        37 to "Two of Cups",
        38 to "Three of Cups",
        39 to "Four of Cups",
        40 to "Five of Cups",
        41 to "Six of Cups",
        42 to "Seven of Cups",
        43 to "Eight of Cups",
        44 to "Nine of Cups",
        45 to "Ten of Cups",
        46 to "Page of Cups",
        47 to "Knight of Cups",
        48 to "Queen of Cups",
        49 to "King of Cups",
        50 to "Ace of Swords",
        51 to "Two of Swords",
        52 to "Three of Swords",
        53 to "Four of Swords",
        54 to "Five of Swords",
        55 to "Six of Swords",
        56 to "Seven of Swords",
        57 to "Eight of Swords",
        58 to "Nine of Swords",
        59 to "Ten of Swords",
        60 to "Page of Swords",
        61 to "Knight of Swords",
        62 to "Queen of Swords",
        63 to "King of Swords",
        64 to "Ace of Pentacles",
        65 to "Two of Pentacles",
        66 to "Three of Pentacles",
        67 to "Four of Pentacles",
        68 to "Five of Pentacles",
        69 to "Six of Pentacles",
        70 to "Seven of Pentacles",
        71 to "Eight of Pentacles",
        72 to "Nine of Pentacles",
        73 to "Ten of Pentacles",
        74 to "Page of Pentacles",
        75 to "Knight of Pentacles",
        76 to "Queen of Pentacles",
        77 to "King of Pentacles"
    )

    suspend fun getSpreadMethod(prompt: String): String {
        val openAI = OpenAI(apikey)
        val id = DAVINCI
        val completionRequest = CompletionRequest(
            model = id,
            prompt = """
                Determine the best tarot card spread method for giving advice on "$prompt"
                Answer in the form "[Spread method name]/[Number of cards needed as an integer]"
            """.trimIndent()
        )
        return openAI.completion(completionRequest).choices.first().text
    }

    suspend fun connect(prompt: String): String {
        val openAI = OpenAI(apikey)
        val id = DAVINCI
        val completionRequest = CompletionRequest(
            model = id,
            prompt = prompt,
            maxTokens = 256
        )
        return openAI.completion(completionRequest).choices.first().text
    }

    fun answer(
        reader: Tarot,
        question: String,
        event: SlashCommandInteractionEvent
    ) {
        runBlocking {
            var response: String = ""
            val request = launch { response = reader.getSpreadMethod(question) }
            request.join()
            val spread: String = response.split("/")[0]
            val cardNumber: Int = response.split("/")[1].toInt()
            val cards: MutableList<String> = mutableListOf()
            for (i in 1..cardNumber) {
                val card = reader.getCard()
                cards.add(card)
            }
            val answer: String = reader.connect(
                """
                            Tarot Card Reading
                            Question: $question
                            Spread Method: $spread
                            Card(s) drawn: ${cards.joinToString(", ")}
                            Give a simple answer and a short explanation in sentences (not lists), followed by a summary.
                        """.trimIndent()
            )
            val message: String =
                "**Question:** $question\n**Spread Method:** ${spread.trim()}\n**Card(s) drawn:** ${
                    cards.joinToString(
                        ", "
                    )
                }\n**Answer:** ${answer.trim()}"
            event.hook.sendMessage(message).queue()
        }
    }

    fun getCard(): String {
        return cards[(0..77).random()]!!
    }
}