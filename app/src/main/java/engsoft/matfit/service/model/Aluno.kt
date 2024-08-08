package engsoft.matfit.service.model

data class Aluno(
    //   @SerializedName("cpf")
    val cpf: String,

    //  @SerializedName("nome")
    var nome: String,

    //   @SerializedName("esporte")
    var esporte: String,

    //   @SerializedName("dataPagamento")
    var dataPagamento: String = "",

    //   @SerializedName("pagamentoAtrasado")
    var pagamentoAtrasado: Boolean = false,
)