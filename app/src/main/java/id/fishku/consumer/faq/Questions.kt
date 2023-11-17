package id.fishku.consumer.faq

class Questions(var question: String, var answer: String) {

    override fun toString(): String {
        return "Questions{" +
                "question='" + question + '\'' +
                ", description='" + answer + '\'' +
                '}'
    }
}
