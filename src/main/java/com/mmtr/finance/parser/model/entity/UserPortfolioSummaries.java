package com.mmtr.finance.parser.model.entity;

import com.mmtr.finance.parser.model.dto.responses.UserPortfolioSummaryResponse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user_portfolio_summaries")
public class UserPortfolioSummaries {

    public UserPortfolioSummaries() {
    }

    public UserPortfolioSummaries(User user) {
        this.user = user;
        date = new Date();
        sumPortfolio = 0;
        percentBasicMaterials = 0;
        percentCommunicationServices = 0;
        percentConsumerCyclical = 0;
        percentConsumerDefensive = 0;
        percentEnergy = 0;
        percentFinancial = 0;
        percentHealthcare = 0;
        percentIndustrials = 0;
        percentRealEstate = 0;
        percentTechnology = 0;
        percentUtilities = 0;
    }

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "sum_portfolio", nullable = false)
    private float sumPortfolio;

    @Column(name = "percent_basic_materials", nullable = false)
    private int percentBasicMaterials;

    @Column(name = "percent_communication_services", nullable = false)
    private int percentCommunicationServices;

    @Column(name = "percentConsumerCyclical", nullable = false)
    private int percentConsumerCyclical;

    @Column(name = "percent_consumer_defensive", nullable = false)
    private int percentConsumerDefensive;

    @Column(name = "percent_energy", nullable = false)
    private int percentEnergy;

    @Column(name = "percent_financial", nullable = false)
    private int percentFinancial;

    @Column(name = "percent_healthcare", nullable = false)
    private int percentHealthcare;

    @Column(name = "percent_industrials", nullable = false)
    private int percentIndustrials;

    @Column(name = "percent_real_estate", nullable = false)
    private int percentRealEstate;

    @Column(name = "percent_technology", nullable = false)
    private int percentTechnology;

    @Column(name = "percent_utilities", nullable = false)
    private int percentUtilities;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public UserPortfolioSummaryResponse getPortfolioSummaryResponse() {
        UserPortfolioSummaryResponse responseModel = new UserPortfolioSummaryResponse();
        responseModel.setSumPortfolio(sumPortfolio);

        responseModel.setPercentBasicMaterials(percentBasicMaterials);
        responseModel.setPercentCommunicationServices(percentCommunicationServices);
        responseModel.setPercentConsumerCyclical(percentConsumerCyclical);
        responseModel.setPercentConsumerDefensive(percentConsumerDefensive);
        responseModel.setPercentEnergy(percentEnergy);
        responseModel.setPercentFinancial(percentFinancial);
        responseModel.setPercentHealthcare(percentHealthcare);
        responseModel.setPercentIndustrials(percentIndustrials);
        responseModel.setPercentRealEstate(percentRealEstate);
        responseModel.setPercentTechnology(percentTechnology);
        responseModel.setPercentUtilities(percentUtilities);

        responseModel.setSumBasicMaterials((percentBasicMaterials * sumPortfolio) / 100);
        responseModel.setSumCommunicationServices((percentCommunicationServices * sumPortfolio) / 100);
        responseModel.setSumConsumerCyclical((percentConsumerCyclical * sumPortfolio) / 100);
        responseModel.setSumConsumerDefensive((percentConsumerDefensive * sumPortfolio) / 100);
        responseModel.setSumEnergy((percentEnergy * sumPortfolio) / 100);
        responseModel.setSumFinancial((percentFinancial * sumPortfolio) / 100);
        responseModel.setSumHealthcare((percentHealthcare * sumPortfolio) / 100);
        responseModel.setSumIndustrials((percentIndustrials * sumPortfolio) / 100);
        responseModel.setSumRealEstate((percentRealEstate * sumPortfolio) / 100);
        responseModel.setSumTechnology((percentTechnology * sumPortfolio) / 100);
        responseModel.setSumUtilities((percentUtilities * sumPortfolio) / 100);

        return responseModel;
    }
}
