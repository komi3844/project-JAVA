CREATE SCHEMA fin_focus;

CREATE TABLE fin_focus.users (
	id bigserial NOT NULL,
	email varchar(255) NOT NULL,
	login varchar(100) NOT NULL,
	"password" varchar(255) NOT NULL,
	"role" varchar NOT NULL DEFAULT 'ROLE_USER'::character varying,
	registration_date date NOT NULL DEFAULT now(),
	CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE fin_focus.recovery_passwords (
	id bigserial NOT NULL,
	"token" varchar(100) NOT NULL,
	user_id int8 NOT NULL,
	expiration_date timestamp(0) NOT NULL,
	CONSTRAINT recovery_passwords_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX recovery_passwords_token_idx ON fin_focus.recovery_passwords USING btree (token);
CREATE INDEX recovery_passwords_user_id_idx ON fin_focus.recovery_passwords USING btree (user_id);
ALTER TABLE fin_focus.recovery_passwords ADD CONSTRAINT recovery_passwords_users_fkey FOREIGN KEY (user_id) REFERENCES fin_focus.users(id);

CREATE TABLE fin_focus.companies (
	id bigserial NOT NULL,
	"name" varchar(255) NOT NULL,
	ticker varchar(255) NOT NULL,
	sector varchar(255) NOT NULL,
	is_actual bool NOT NULL DEFAULT true,
	CONSTRAINT companies_pkey PRIMARY KEY (id)
);

CREATE TABLE fin_focus.indicators (
	id bigserial NOT NULL,
	company_id int8 NOT NULL,
	"date" date NOT NULL DEFAULT now(),
	index_company varchar NULL,
	market_cap float4 NULL,
	income float4 NULL,
	sales float4 NULL,
	book_per_share float4 NULL,
	cash_per_share float4 NULL,
	dividend float4 NULL,
	dividend_percent float4 NULL,
	employees int4 NULL,
	recommendation float4 NULL,
	price_to_earnings float4 NULL,
	forward_price_to_earnings float4 NULL,
	peg float4 NULL,
	price_to_sales float4 NULL,
	price_to_book float4 NULL,
	price_to_cash_per_share float4 NULL,
	price_to_free_cash_flow float4 NULL,
	quick_ratio float4 NULL,
	current_ratio float4 NULL,
	debt_to_equity float4 NULL,
	long_term_debt_to_equity float4 NULL,
	twenty_days_simple_moving float4 NULL,
	diluted_eps float4 NULL,
	next_year_estimate_eps float4 NULL,
	next_quartal_estimate_eps float4 NULL,
	this_year_eps float4 NULL,
	next_year_eps float4 NULL,
	next_five_years_eps float4 NULL,
	past_five_years_eps float4 NULL,
	sales_past_five_years float4 NULL,
	quartal_revenue_growth float4 NULL,
	quarterly_earnings_growth float4 NULL,
	fiftieth_days_simple_moving float4 NULL,
	insider_own float4 NULL,
	insider_trans float4 NULL,
	inst_own float4 NULL,
	inst_trans float4 NULL,
	roa float4 NULL,
	roe float4 NULL,
	roi float4 NULL,
	gross_margin float4 NULL,
	operating_margin float4 NULL,
	profit_margin float4 NULL,
	dividend_payout_ratio float4 NULL,
	two_hundredth_days_simple_moving float4 NULL,
	target_price float4 NULL,
	performance_week float4 NULL,
	performance_month float4 NULL,
	performance_quarter float4 NULL,
	performance_half_year float4 NULL,
	performance_year float4 NULL,
	performance_ytd float4 NULL,
	price float4 NULL,
	CONSTRAINT indicators_pkey PRIMARY KEY (id)
);

CREATE INDEX indicators_company_id_idx ON fin_focus.indicators USING btree (company_id);
ALTER TABLE fin_focus.indicators ADD CONSTRAINT indicators_companies_fkey FOREIGN KEY (company_id) REFERENCES fin_focus.companies(id);


CREATE TABLE fin_focus.currencies (
	id bigserial NOT NULL,
	code varchar(10) NOT NULL,
	exchange_rate float8 NOT NULL,
	tmstmp timestamp(0) NOT NULL
);
CREATE UNIQUE INDEX currencies_code ON fin_focus.currencies USING btree (code);


CREATE TABLE fin_focus.portfolios (
	id bigserial NOT NULL,
	company_id int8 NOT NULL,
	user_id int8 NOT NULL,
	count int4 NOT NULL DEFAULT 1,
	CONSTRAINT user2company_pkey PRIMARY KEY (id)
);
CREATE INDEX ON fin_focus.portfolios USING btree (user_id);
CREATE INDEX ON fin_focus.portfolios USING btree (company_id);


ALTER TABLE fin_focus.portfolios ADD CONSTRAINT user2company_companies_fkey FOREIGN KEY (company_id) REFERENCES fin_focus.companies(id);
ALTER TABLE fin_focus.portfolios ADD CONSTRAINT user2company_users_fkey FOREIGN KEY (user_id) REFERENCES fin_focus.users(id);


CREATE TABLE fin_focus.user_portfolio_summaries (
	id bigserial NOT NULL,
	user_id int8 NOT NULL,
	sum_portfolio int4 NOT NULL DEFAULT 0,
	percent_basic_materials int4 NOT NULL DEFAULT 0,
	percent_communication_services int4 NOT NULL DEFAULT 0,
	percent_consumer_cyclical int4 NOT NULL DEFAULT 0,
	percent_consumer_defensive int4 NOT NULL DEFAULT 0,
	percent_energy int4 NOT NULL DEFAULT 0,
	percent_financial int4 NOT NULL DEFAULT 0,
	percent_healthcare int4 NOT NULL DEFAULT 0,
	percent_industrials int4 NOT NULL DEFAULT 0,
	percent_real_estate int4 NOT NULL DEFAULT 0,
	percent_technology int4 NOT NULL DEFAULT 0,
	percent_utilities int4 NOT NULL DEFAULT 0,
	"date" date NOT NULL DEFAULT now(),
	CONSTRAINT user_portfolio_summaries_pkey PRIMARY KEY (id),
	CONSTRAINT user_portfolio_summaries_user_id_date_key UNIQUE (user_id, date)
);

CREATE INDEX user_portfolio_summaries_user_id_idx ON fin_focus.user_portfolio_summaries USING btree (user_id);
ALTER TABLE fin_focus.user_portfolio_summaries ADD CONSTRAINT user_portfolio_summaries_users_fkey FOREIGN KEY (user_id) REFERENCES fin_focus.users(id);
