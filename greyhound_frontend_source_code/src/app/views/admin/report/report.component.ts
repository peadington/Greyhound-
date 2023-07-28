import { Component, OnInit } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { Pagination } from "app/shared/interfaces/pagination.interface";
import { UtilityService } from "app/shared/services/utility.service";
import { environment } from "environments/environment";
import { GreyhoundService } from "../greyhound/greyhound.service";
import { TrainerService } from "../trainer/trainer.service";
import { ReportService } from "./report.service";

@Component({
  selector: "app-report",
  templateUrl: "./report.component.html",
  styleUrls: ["./report.component.scss"],
})
export class ReportComponent implements OnInit {
  formGroup!: FormGroup;
  isLoading: boolean = false;
  displayedColumns: any = [];
  displayedTrainerColumns: any[] = [];
  distanceData: any[] = [];
  minEntryData: any;
  orderData: any[] = [{ key: "By STmHcp", value: 'stmhcp' }, { key: "By WinTime", value: 'wintime' }, { key: "By CalcTm", value: 'calctm' }, { key: "By Rating", value: 'rating' }];
  gradeData: any[] = [];
  chartKindData: any[] = [
    "Position by date",
    "Weight by date",
    "SctHcp time by date",
    "Calc by date",
  ];
  trackData: any[] = [];
  filterValue: any;
  dataSource: any[] = [];
  pagination: Pagination;
  trainerList: any[] = [];
  filteredTrainerList: any[] = [];
  curruncySymbol = environment.curruncySymbol
  disableDistance: boolean = false

  constructor(
    private _reportService: ReportService,
    private _greyhoundService: GreyhoundService,
    public _utilityService: UtilityService,
    private _trainerService: TrainerService
  ) { }

  ngOnInit(): void {
    this.pagination = this._utilityService.pagination
    this.formGroup = this._reportService.createFilterForm(null);
    this.displayedColumns = this._reportService.displayedColumns;
    this.displayedTrainerColumns = this._reportService.displayedTrainerColumns;

    this.search()
    this.getAllTracks();
    // this.getAllDistance();
    // this.getAllGrade();
  }

  getAllTracks() {
    this.isLoading = true
    this._greyhoundService.getTracks().then((response: any) => {
      this.trackData = response.body.data;
      this.trackData.sort(function (a, b) {
        if (a.name < b.name) { return -1; }
        // if(a.firstname > b.firstname) { return 1; }
        return 0;
      })
      this.isLoading = false
    });

  }

  async onFromDateChange(date) {
    let data = this.formGroup.getRawValue();
    let requestBody = {
      track: [data.track],
      fromDate: date,
      toDate: data.toDate
    }
    await this.getTrainersByFilter(requestBody);
  }

  async onToDateChange(date) {
    let data = this.formGroup.getRawValue();
    let requestBody = {
      track: [data.track],
      fromDate: data.fromDate,
      toDate: date
    }
    await this.getTrainersByFilter(requestBody);
  }

  async getTrainersByFilter(requestBody: any) {
    this.isLoading = true;
    this.filteredTrainerList = [];
    this.distanceData = [];
    this.gradeData = [];
    await this._trainerService.getTrainerListByDateAndTrack(requestBody).then((response: any) => {
      this.filteredTrainerList = response.body.data;
      this.filteredTrainerList.sort(function (a, b) {
        if (a.name < b.name) { return -1; }
        // if(a.firstname > b.firstname) { return 1; }
        return 0;
      })
      this.isLoading = false;
    })
  }

  getAllDistance() {
    this.isLoading = true
    this._greyhoundService.getDistance().then((response: any) => {
      this.distanceData = response.body.data;
      this.distanceData.sort(function (a, b) {
        if (a < b) { return -1; }
        // if(a.firstname > b.firstname) { return 1; }
        return 0;
      })
      this.isLoading = false
    });

  }

  getAllGrade() {
    this.isLoading = true
    this._greyhoundService.getGrade().then((response: any) => {
      this.gradeData = response.body.data;
      this.gradeData.sort(function (a, b) {
        if (a < b) { return -1; }
        // if(a.firstname > b.firstname) { return 1; }
        return 0;
      })
      this.isLoading = false
    });

  }

  /**
  * Get next page data
  * 
  * @param page 
  */
  getNextPageData(page: any) {
    this.pagination.currentPage = page.pageIndex + 1;
    this.pagination.perPage = page.pageSize;
    this.search();
  }

  async onSelectionChangeTrack(track) {
    const data = this.formGroup.getRawValue();
    let requestBody = {
      track: [track],
      fromDate: data.fromDate,
      toDate: data.toDate
    }
    await this.getTrainersByFilter(requestBody);
  }

  reset() {
    this.formGroup.reset();
    this.formGroup = this._reportService.createFilterForm(null);
    this.dataSource = [];
  }

  onSelectionChangeTrainer() {
    this.pagination.data = [];
    this.dataSource = [];
    this.isLoading = true;
    let data = this.formGroup.getRawValue();
    let searchData = {
      filter: data,
      pagination: this.pagination
    }
    this._reportService.getDistancesByFilter(data).then((response: any) => {
      this.distanceData = response.body.data;
      this.distanceData.sort(function (a, b) {
        if (a < b) { return -1; }
        // if(a.firstname > b.firstname) { return 1; }
        return 0;
      })
      this.isLoading = false;
    }, error => {
      this.isLoading = false;
    });

    this._reportService.getGradesByFilter(data).then((response: any) => {
      this.gradeData = response.body.data;
      this.gradeData.sort(function (a, b) {
        if (a < b) { return -1; }
        // if(a.firstname > b.firstname) { return 1; }
        return 0;
      })
      this.isLoading = false;
    }, error => {
      this.isLoading = false;
    });
  }

  search() {
    this.pagination.data = [];
    this.dataSource = [];
    this.isLoading = true;
    let data = this.formGroup.getRawValue();
    let searchData = {
      filter: data,
      pagination: this.pagination
    }
    this._reportService.getReport(searchData).then((response: any) => {
      this.pagination = response.body.data,
        this.dataSource = this.pagination.data
      if (data.order === 'calctm') {
        this.dataSource.sort(function (a, b) { return a.calcTm - b.calcTm });
      }
      if (data.order === 'wintime') {
        this.dataSource.sort(function (a, b) { return a.winTime - b.winTime });
      }
      if (data.order === 'stmhcp') {
        this.dataSource.sort(function (a, b) { return a.stmhcp - b.stmhcp });
      }
      if (data.order === 'rating') {
        this.dataSource.sort(function (a, b) { return a.rating - b.rating });
      }
      this.isLoading = false;
    }, error => {
      this.isLoading = false;
    });
  }

  export() {
    this.isLoading = true;
    let data = this.formGroup.getRawValue();
    this._reportService.exportReport(data).then((response: any) => {
      let dataSource = response.body.data
      // if (data.order === 'calctm') {
      //   dataSource.sort(function (a, b) { return a.calcTm - b.calcTm });
      // }
      // if (data.order === 'wintime') {
      //   dataSource.sort(function (a, b) { return a.winTime - b.winTime });
      // }
      // if (data.order === 'stmhcp') {
      //   dataSource.sort(function (a, b) { return a.stmhcp - b.stmhcp });
      // }
      this._reportService.export(dataSource);
      this.isLoading = false;
    }, error => {
      this.isLoading = false;
    });
  }

}
