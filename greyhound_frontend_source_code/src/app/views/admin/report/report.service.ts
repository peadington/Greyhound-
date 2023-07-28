import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiService } from 'app/shared/services/api.service';
import { UtilityService } from '../../../shared/services/utility.service';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  displayedColumns: any[] = ['rank', 'greyhoundName', 'entryNum', 'avRank', 'stmCp', 'winTime', 'calTm', 'weight', 'prize'];
  displayedTrainerColumns: any[] = ['rank', 'trainer', 'winNum', 'avRank', 'totalPrize', 'note'];
  constructor(
    private _formBuilder: FormBuilder,
    private _apiUrl: ApiService,
    private _utilityService: UtilityService
  ) { }

  createFilterForm(element: any): FormGroup {
    const date = new Date(), y = date.getFullYear(), m = date.getMonth();
    const fromDate = new Date(y, m - 1, date.getDate());
    const toDate = new Date(y, m, date.getDate());
    return this._formBuilder.group({
      fromDate: [fromDate, [Validators.required]],
      toDate: [toDate, [Validators.required]],
      track: 'all',
      trainer: '0',
      grade: 'all',
      distance: '0',
      order: 'stmhcp'
    })
  }

  getReport(data: any) {
    return this._apiUrl.post(data, `report/get`)
  }

  getDistancesByFilter(data: any) {
    return this._apiUrl.post(data, `race/get/distance`)
  }

  getGradesByFilter(data: any) {
    return this._apiUrl.post(data, `race/get/grades`)
  }

  exportReport(data: any) {
    return this._apiUrl.post(data, `report/export`);
  }

  export(data: any) {
    let dataList: any = [];
    data.forEach(element => {
      let json = {};
      json['Greyhound Name'] = element.greyhoundName;
      json['Entry Number'] = element.entryNum;
      json['Sectional Time'] = element.stmhcp ? Math.round(element.stmhcp * 100) / 100 : 0;
      json['Win Time'] = element.winTime ? Math.round(element.winTime * 100) / 100 : 0;
      json['Weight'] = element.weight ? Math.round(element.weight * 100) / 100 : 0;
      json['Calculated Time'] = element.calcTm ? Math.round(element.calcTm * 100) / 100 : 0;
      json['Rating'] = element.rating ? Math.round(element.rating * 100) / 100 : 0;
      dataList.push(json);
    });
    this._utilityService.exportToExcel(dataList, 'Report');
  }
}