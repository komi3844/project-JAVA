package com.mmtr.finance.parser.model.dto.responses;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPortfolioSummaryResponse {
    @ApiModelProperty(notes = "Общая сумма по портфелю, $")
    private float sumPortfolio;

    @ApiModelProperty(notes = "Процент по сектору основных материалов, %")
    private int percentBasicMaterials;

    @ApiModelProperty(notes = "Процент по сектору коммуникационных услуг, %")
    private int percentCommunicationServices;

    @ApiModelProperty(notes = "Процент по потребительсокму сектору (циклическому), %")
    private int percentConsumerCyclical;

    @ApiModelProperty(notes = "Процент по потребительскому сектору (защитному), %")
    private int percentConsumerDefensive;

    @ApiModelProperty(notes = "Процент по сектору энергетики , %")
    private int percentEnergy;

    @ApiModelProperty(notes = "Процент по финансовому сектору , %")
    private int percentFinancial;

    @ApiModelProperty(notes = "Процент по сектору здравоохранения, %")
    private int percentHealthcare;

    @ApiModelProperty(notes = "Процент по промышленному сектору, %")
    private int percentIndustrials;

    @ApiModelProperty(notes = "Процент по сектору недвижимости, %")
    private int percentRealEstate;

    @ApiModelProperty(notes = "Процент по сектору технологий, %")
    private int percentTechnology;

    @ApiModelProperty(notes = "Процент по сектору коммунальных услуг , %")
    private int percentUtilities;

    @ApiModelProperty(notes = "Сумма по сектору основных материалов, $")
    private float sumBasicMaterials;

    @ApiModelProperty(notes = "Сумма по сектору коммуникационных услуг, $")
    private float sumCommunicationServices;

    @ApiModelProperty(notes = "Сумма по потребительсокму сектору (циклическому), $")
    private float sumConsumerCyclical;

    @ApiModelProperty(notes = "Сумма по потребительскому сектору (защитному), $")
    private float sumConsumerDefensive;

    @ApiModelProperty(notes = "Сумма по сектору энергетики, $")
    private float sumEnergy;

    @ApiModelProperty(notes = "Сумма по финансовому сектор, $")
    private float sumFinancial;

    @ApiModelProperty(notes = "Сумма по сектору здравоохранения, $")
    private float sumHealthcare;

    @ApiModelProperty(notes = "Сумма по промышленному сектору, $")
    private float sumIndustrials;

    @ApiModelProperty(notes = "Сумма по сектору недвижимости, $")
    private float sumRealEstate;

    @ApiModelProperty(notes = "Сумма по сектору технологий, $")
    private float sumTechnology;

    @ApiModelProperty(notes = "Сумма по сектору коммунальных услуг, $")
    private float sumUtilities;
}
