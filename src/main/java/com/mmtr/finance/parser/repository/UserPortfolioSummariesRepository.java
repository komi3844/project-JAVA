package com.mmtr.finance.parser.repository;

import com.mmtr.finance.parser.model.entity.User;
import com.mmtr.finance.parser.model.entity.UserPortfolioSummaries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Repository
public interface UserPortfolioSummariesRepository extends JpaRepository<UserPortfolioSummaries, Long> {
    @Query(value = "SELECT *" +
            "FROM fin_focus.user_portfolio_summaries " +
            "WHERE ID = (SELECT MAX(ID) " +
            "FROM fin_focus.user_portfolio_summaries " +
            "WHERE user_id = :userId)", nativeQuery = true)
    Optional<UserPortfolioSummaries> findTopByUserOrderByIdDesc(long userId);

    Optional<UserPortfolioSummaries> findByUserAndDate(User user, Date date);

    @Modifying
    @Query(value = "INSERT INTO fin_focus.user_portfolio_summaries (user_id, " +
            "sum_portfolio, " +
            "percent_basic_materials, " +
            "percent_communication_services, " +
            "percent_consumer_cyclical, " +
            "percent_consumer_defensive, " +
            "percent_energy, " +
            "percent_financial, " +
            "percent_healthcare, " +
            "percent_industrials, " +
            "percent_real_estate, " +
            "percent_technology, " +
            "percent_utilities, " +
            "\"date\") " +
            "WITH all_infos AS\n" +
            "  (SELECT uc.count AS \"count_stocks\",\n" +
            "          c.sector AS \"sector\",\n" +
            "          i.price AS \"price_per_stock\"\n" +
            "   FROM fin_focus.portfolios uc\n" +
            "   JOIN fin_focus.companies c ON c.id = uc.company_id\n" +
            "   JOIN fin_focus.indicators i ON (i.id =\n" +
            "                                     (SELECT max(id)\n" +
            "                                      FROM fin_focus.indicators i2\n" +
            "                                      WHERE company_id = c.id))\n" +
            "   WHERE c.is_actual = TRUE\n" +
            "     AND uc.user_id = :userId),\n" +
            "sum_portfolio AS\n" +
            "  (SELECT sum(ai.count_stocks * ai.price_per_stock) AS \"sum_portfolio\"\n" +
            "   FROM all_infos ai),\n" +
            "sum_sectors AS\n" +
            "  (SELECT sum(ai.count_stocks * ai.price_per_stock) AS \"sum_sector\",\n" +
            "          ai.sector\n" +
            "   FROM all_infos ai\n" +
            "   GROUP BY ai.sector)\n" +
            "SELECT :userId,\n" +
            "       coalesce(round(CAST(sp.sum_portfolio AS numeric), 2), 0),\n" +
            "       coalesce(round(CAST(\n" +
            "                             (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                              FROM sum_sectors ss\n" +
            "                              WHERE ss.sector = 'Основные материалы') AS numeric), 2)*100, 0),\n" +
            "       coalesce(round(CAST(\n" +
            "                             (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                              FROM sum_sectors ss\n" +
            "                              WHERE ss.sector = 'Коммуникационные услуги') AS numeric), 2)*100, 0),\n" +
            "       coalesce(round(CAST(\n" +
            "                             (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                              FROM sum_sectors ss\n" +
            "                              WHERE ss.sector = 'Потребительский (цикличный)') AS numeric), 2) *100, 0),\n" +
            "       coalesce(round(CAST(\n" +
            "                             (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                              FROM sum_sectors ss\n" +
            "                              WHERE ss.sector = 'Потребительский (защитный)') AS numeric), 2)*100, 0),\n" +
            "       coalesce(round(CAST(\n" +
            "                             (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                              FROM sum_sectors ss\n" +
            "                              WHERE ss.sector = 'Энергетика') AS numeric), 2)*100, 0),\n" +
            "       coalesce(round(CAST(\n" +
            "                             (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                              FROM sum_sectors ss\n" +
            "                              WHERE ss.sector = 'Финансы') AS numeric), 2)*100, 0),\n" +
            "       coalesce(round(CAST(\n" +
            "                             (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                              FROM sum_sectors ss\n" +
            "                              WHERE ss.sector = 'Здравоохранение') AS numeric), 2)*100, 0),\n" +
            "       coalesce(round(CAST(\n" +
            "                             (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                              FROM sum_sectors ss\n" +
            "                              WHERE ss.sector = 'Промышленность') AS numeric), 2)*100, 0),\n" +
            "       coalesce(round(CAST(\n" +
            "                             (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                              FROM sum_sectors ss\n" +
            "                              WHERE ss.sector = 'Недвижимость') AS numeric), 2)*100, 0),\n" +
            "       coalesce(round(CAST(\n" +
            "                             (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                              FROM sum_sectors ss\n" +
            "                              WHERE ss.sector = 'Технологии') AS numeric), 2)*100, 0),\n" +
            "       coalesce(round(CAST(\n" +
            "                             (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                              FROM sum_sectors ss\n" +
            "                              WHERE ss.sector = 'Коммунальные услуги') AS numeric), 2)*100, 0),\n" +
            "       to_date(:date, 'DD-MM-YYYY')\n" +
            "FROM sum_portfolio sp;", nativeQuery = true)
    @Transactional
    void createUserPortfolioSummary(@Param("userId") long userId,
                                    @Param("date") String date);


    @Modifying
    @Query(value = "WITH all_infos AS\n" +
            "  (SELECT uc.count AS \"count_stocks\",\n" +
            "          c.sector AS \"sector\",\n" +
            "          i.price AS \"price_pe_stock\"\n" +
            "   FROM fin_focus.portfolios uc\n" +
            "   JOIN fin_focus.companies c ON c.id = uc.company_id\n" +
            "   JOIN fin_focus.indicators i ON (i.id =\n" +
            "                                     (SELECT max(id)\n" +
            "                                      FROM fin_focus.indicators i2\n" +
            "                                      WHERE company_id = c.id))\n" +
            "   WHERE c.is_actual = TRUE\n" +
            "     AND uc.user_id = :userId),\n" +
            "     sum_portfolio AS\n" +
            "  (SELECT sum(ai.count_stocks * ai.price_pe_stock) AS \"sum_portfolio\"\n" +
            "   FROM all_infos ai),\n" +
            "     sum_sectors AS\n" +
            "  (SELECT sum(ai.count_stocks * ai.price_pe_stock) AS \"sum_sector\",\n" +
            "          ai.sector\n" +
            "   FROM all_infos ai\n" +
            "   GROUP BY ai.sector)\n" +
            "UPDATE fin_focus.user_portfolio_summaries\n" +
            "SET sum_portfolio =\n" +
            "  (SELECT coalesce(round(CAST(sp.sum_portfolio AS numeric), 2), 0)\n" +
            "   FROM sum_portfolio sp),\n" +
            "    percent_basic_materials =\n" +
            "  (SELECT coalesce(round(CAST(\n" +
            "                                (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                                 FROM sum_sectors ss, sum_portfolio sp\n" +
            "                                 WHERE ss.sector = 'Основные материалы') AS numeric), 2)*100, 0)),\n" +
            "    percent_communication_services =\n" +
            "  (SELECT coalesce(round(CAST(\n" +
            "                                (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                                 FROM sum_sectors ss, sum_portfolio sp\n" +
            "                                 WHERE ss.sector = 'Коммуникационные услуги') AS numeric), 2)*100, 0)),\n" +
            "    percent_consumer_cyclical =\n" +
            "  (SELECT coalesce(round(CAST(\n" +
            "                                (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                                 FROM sum_sectors ss, sum_portfolio sp\n" +
            "                                 WHERE ss.sector = 'Потребительский (цикличный)') AS numeric), 2) *100, 0)),\n" +
            "    percent_consumer_defensive =\n" +
            "  (SELECT coalesce(round(CAST(\n" +
            "                                (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                                 FROM sum_sectors ss, sum_portfolio sp\n" +
            "                                 WHERE ss.sector = 'Потребительский (защитный)') AS numeric), 2)*100, 0)),\n" +
            "    percent_energy =\n" +
            "  (SELECT coalesce(round(CAST(\n" +
            "                                (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                                 FROM sum_sectors ss, sum_portfolio sp\n" +
            "                                 WHERE ss.sector = 'Энергетика') AS numeric), 2)*100, 0)),\n" +
            "    percent_financial =\n" +
            "  (SELECT coalesce(round(CAST(\n" +
            "                                (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                                 FROM sum_sectors ss, sum_portfolio sp\n" +
            "                                 WHERE ss.sector = 'Финансы') AS numeric), 2)*100, 0)),\n" +
            "    percent_healthcare =\n" +
            "  (SELECT coalesce(round(CAST(\n" +
            "                                (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                                 FROM sum_sectors ss, sum_portfolio sp\n" +
            "                                 WHERE ss.sector = 'Здравоохранение') AS numeric), 2)*100, 0)),\n" +
            "    percent_industrials =\n" +
            "  (SELECT coalesce(round(CAST(\n" +
            "                                (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                                 FROM sum_sectors ss, sum_portfolio sp\n" +
            "                                 WHERE ss.sector = 'Промышленность') AS numeric), 2)*100, 0)),\n" +
            "    percent_real_estate =\n" +
            "  (SELECT coalesce(round(CAST(\n" +
            "                                (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                                 FROM sum_sectors ss, sum_portfolio sp\n" +
            "                                 WHERE ss.sector = 'Недвижимость') AS numeric), 2)*100, 0)),\n" +
            "    percent_technology =\n" +
            "  (SELECT coalesce(round(CAST(\n" +
            "                                (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                                 FROM sum_sectors ss, sum_portfolio sp\n" +
            "                                 WHERE ss.sector = 'Технологии') AS numeric), 2)*100, 0)),\n" +
            "    percent_utilities =\n" +
            "  (SELECT coalesce(round(CAST(\n" +
            "                                (SELECT ss.sum_sector/sp.sum_portfolio\n" +
            "                                 FROM sum_sectors ss, sum_portfolio sp\n" +
            "                                 WHERE ss.sector = 'Коммунальные услуги') AS numeric), 2)*100, 0))\n" +
            "WHERE ID = :id", nativeQuery = true)
    @Transactional
    void updateUserPortfolioSummary(@Param("id") long id,
                                    @Param("userId") long userId);
}