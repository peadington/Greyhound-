import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ApiService } from 'app/shared/services/api.service';

@Injectable({
  providedIn: 'root'
})
export class CompareService {
  // displayedColumns:any[]=['rank','greyhoundName','trainer','entryNum','avRank', 'stmCp','winTime', 'calTm', 'note'];
  // displayedTrainerColumns:any[]=['rank','trainer','winNum','avRank', 'totalPrize', 'note'];
  constructor(
    private _formBuilder  : FormBuilder,
    private _apiUrl : ApiService
  ) { }

 

  createFilterForm(element: any): FormGroup{
    const date = new Date(), y = date.getFullYear(), m = date.getMonth();
    const firstDay = new Date(y, m, 1);
    const lastDay = new Date(y, m + 1, 0);
    return this._formBuilder.group({    
      name: element ? [element.name]:null,
      chartKind: element ? [element.chartKind]:"weight",
      fromDate: element ? [element.fromDate]:firstDay,
      toDate: element ? [element.toDate]:lastDay,
  })
}
getGreyhoundSearchByName(){

    return this._apiUrl.get(`greyhound/searchByName`)
  
}
getGreyhoundReport(data:any){
  return this._apiUrl.post(data, `greyhound/getGreyhoundReportByGreyhoundIdAndChartKind`)
}
}
