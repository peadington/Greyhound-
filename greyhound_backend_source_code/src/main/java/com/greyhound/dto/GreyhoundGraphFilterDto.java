package com.greyhound.dto;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author p4logics
 *
 */
public class GreyhoundGraphFilterDto {
	private List<Long> greyhoundIds;
	private String chartKind;
	private Date fromDate;
	private Date toDate;

	public List<Long> getGreyhoundIds() {
		return greyhoundIds;
	}

	public void setGreyhoundIds(List<Long> greyhoundIds) {
		this.greyhoundIds = greyhoundIds;
	}

	public String getChartKind() {
		return chartKind;
	}

	public void setChartKind(String chartKind) {
		this.chartKind = chartKind;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
