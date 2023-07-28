package com.greyhound.dto;

/**
 * 
 * @author p4logics
 *
 */
public class GreyhoundFilterWithPagination {
	private GreyhoundFilterDto filter;
	private PaginationDto pagination;

	public GreyhoundFilterDto getFilter() {
		return filter;
	}

	public void setFilter(GreyhoundFilterDto filter) {
		this.filter = filter;
	}

	public PaginationDto getPagination() {
		return pagination;
	}

	public void setPagination(PaginationDto pagination) {
		this.pagination = pagination;
	}

}
