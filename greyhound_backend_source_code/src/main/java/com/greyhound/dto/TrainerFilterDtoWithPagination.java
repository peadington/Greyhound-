package com.greyhound.dto;

/**
 * 
 * @author p4logics
 *
 */
public class TrainerFilterDtoWithPagination {
	private TrainerFilterDto filter;
	private PaginationDto pagination;

	public TrainerFilterDto getFilter() {
		return filter;
	}

	public void setFilter(TrainerFilterDto filter) {
		this.filter = filter;
	}

	public PaginationDto getPagination() {
		return pagination;
	}

	public void setPagination(PaginationDto pagination) {
		this.pagination = pagination;
	}

}
