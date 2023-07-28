package com.greyhound.dto;

/**
 * 
 * @author p4logics
 *
 */
public class ReportFilterDtoWithPagination {
	private ReportRequestDto filter;
	private PaginationDto pagination;

	public ReportRequestDto getFilter() {
		return filter;
	}

	public void setFilter(ReportRequestDto filter) {
		this.filter = filter;
	}

	public PaginationDto getPagination() {
		return pagination;
	}

	public void setPagination(PaginationDto pagination) {
		this.pagination = pagination;
	}

}
