package team.flex.training.corehr.assignment.query

import team.flex.training.corehr.company.CompanyModel

data class CompanyDto(
    val companyId: Long,
    val companyName: String,
) {
    companion object {
        fun from(company: CompanyModel): CompanyDto = CompanyDto(company.companyId, company.name)
    }
}
