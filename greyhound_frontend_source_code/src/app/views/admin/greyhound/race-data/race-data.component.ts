import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Pagination } from 'app/shared/interfaces/pagination.interface';
import { UtilityService } from 'app/shared/services/utility.service';
import { GreyhoundService } from '../greyhound.service';

@Component({
  selector: 'app-race-data',
  templateUrl: './race-data.component.html',
  styleUrls: ['./race-data.component.scss']
})
export class RaceDataComponent {
  displayedRaceColumns:any[]=[];
  raceDataSource:any;
  pagination : Pagination;
  isLoading: boolean= false

  constructor(private _greyhoundService: GreyhoundService,
    public matDialogRef: MatDialogRef<RaceDataComponent>,
    @Inject(MAT_DIALOG_DATA) private _data: any,
    public _utilityService : UtilityService)
  {}

  ngOnInit(): void {
    this.pagination= this._utilityService.pagination
    this.displayedRaceColumns= this._greyhoundService.displayedRaceColumns,
    this.getRaceDetails(this._data)
  }

      /**
   * Get next page data
   * 
   * @param page 
   */
       getNextPageData(page: any) {
        this.pagination.currentPage = page.pageIndex + 1;
        this.pagination.perPage = page.pageSize;
        this.getRaceDetails(this._data);
      }

      getRaceDetails(element:any){
        this.isLoading=true
        let data= this.pagination
        this._greyhoundService.getRaceDetails(data, element.greyhoundId).then((response:any)=>{
          this.pagination = response.body.data;
          this.raceDataSource = this.pagination.data;

          this.isLoading=false
        })
      }

}
