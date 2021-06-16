package com.example.covid19.data.entity

data class GraphDataModel(
    var active: Int = 0,
    var caseType: CaseType = CaseType.NewCases(),
    var date: String = ""
)

fun CovidCasesModel.mapToGraphData(caseType: CaseType) =
    GraphDataModel(active = if (caseType is CaseType.NewCases) infected else dead, date = date)
