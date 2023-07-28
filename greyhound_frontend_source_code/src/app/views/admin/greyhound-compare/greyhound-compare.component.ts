import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Pagination } from '../../../shared/interfaces/pagination.interface';
import { UtilityService } from '../../../shared/services/utility.service';
import { CompareService } from '../compare/compare.service';
import { GreyhoundService } from '../greyhound/greyhound.service';
import { ReportService } from '../report/report.service';
import { TrainerService } from '../trainer/trainer.service';

import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexTitleSubtitle
} from "ng-apexcharts";

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  title: ApexTitleSubtitle;
};

@Component({
  selector: 'app-greyhound-compare',
  templateUrl: './greyhound-compare.component.html',
  styleUrls: ['./greyhound-compare.component.scss']
})
export class GreyhoundCompareComponent {
  formGroup!: FormGroup;
  isLoading: boolean = false;
  displayedColumns: any = [];
  displayedTrainerColumns: any[] = [];
  whenData: any[] = [
    { key: 7, value: "7 days" }, { key: 28, value: "28 days" }, { key: 12, value: "12 days" }];
  distanceData: any[] = [];
  minEntryData: any;
  greyhoundIds: any
  shftesVar: any[] = []
  greyhoundId: any
  greyhoundNames: any[] = []
  selectedNames: any = []
  orderData: any[] = [];
  gradeData: any[] = [];
  chartKindData: any[] = [{ key: "By Rating", value: 'rating' }, { key: "By STmHcp", value: 'stmhcp' }, { key: "By WinTime", value: 'wintime' }, { key: "By CalcTm", value: 'calctm' }];
  trainerDataSource: any[] = [];
  greyhoundDataSource: any[] = [];
  pagination: Pagination;
  trackList: any[] = [];
  trainerList: any[] = [];
  filteredTrainerList: any[] = [];
  greyhoundList: any[] = [];
  filteredGreyhoundList: any[] = [];

  lineChartSteppedData: Array<any> = [{
    data: [],
    label: '',
    borderWidth: 0,
    fill: true,
    backgroundColor: ["navy", "black", "navy"],
    hoverBackgroundColor: ["darknavy", "darkblack", "darknavy"],
    // steppedLine: true
  }];
  public lineChartLabels: Array<any> = [];
  public lineChartOptions: any = {
    responsive: true,
    maintainAspectRatio: false,
    legend: {
      display: false,
      position: 'bottom'
    },
    scales: {
      xAxes: [{
        display: false,
        gridLines: {
          color: 'rgba(0,0,0,0.02)',
          zeroLineColor: 'rgba(0,0,0,0.02)'
        }
      }],
      yAxes: [{
        display: false,
        gridLines: {
          color: 'rgba(0,0,0,0.02)',
          zeroLineColor: 'rgba(0,0,0,0.02)'
        },
        ticks: {
          beginAtZero: true,
          suggestedMax: 9,
        }
      }]
    }
  };

  public lineChartColors: Array<any> = [];
  public lineChartLegend: boolean = true;
  public lineChartType: string = 'line';
  greyhoundData: any[] = [];

  @ViewChild("chart") chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;
  yacesData: any[] = [];

  constructor(
    private _reportService: ReportService,
    private _greyhoundService: GreyhoundService,
    public _utilityService: UtilityService,
    private _trainerService: TrainerService,
    private _compareService: CompareService
  ) { }

  ngOnInit(): void {
    this.pagination = this._utilityService.pagination
    this.formGroup = this._greyhoundService.greyhoundCompareFilterForm(null);
    this.displayedColumns = this._reportService.displayedColumns;
    this.displayedTrainerColumns = this._reportService.displayedTrainerColumns;
    this.getAllTracks();
    this.getAllDistance();
  }

  async getAllDistance() {
    await this._trainerService.getDistanceData().then((response: any) => {
      this.distanceData = response.body.data;
      this.distanceData.sort(function (a, b) {
        if (a < b) { return -1; }
        return 0;
      })
    })
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

  async onSelectionChangeTrack(track: any) {
    let data = this.formGroup.getRawValue();
    let requestBody = {
      track: [track],
      fromDate: data.fromDate,
      toDate: data.toDate
    }
    await this.getTrainersByFilter(requestBody);
  }

  async getTrainersByFilter(requestBody: any) {
    this.formGroup.controls.trainer.setValue([]);
    this.isLoading = true;
    this.trainerList = [];
    this.filteredTrainerList = [];
    this.filteredGreyhoundList = [];
    this.greyhoundList = [];
    await this._trainerService.getTrainerListByDateAndTrack(requestBody).then((response: any) => {
      this.trainerList = response.body.data;
      this.filteredTrainerList = this.trainerList;
      this.filteredTrainerList.sort(function (a, b) {
        if (a.name < b.name) { return -1; }
        // if(a.firstname > b.firstname) { return 1; }
        return 0;
      })
      this.isLoading = false;
    })
  }

  async onFromDateChange(date) {
    let data = this.formGroup.getRawValue();
    // let requestBody = {
    //   fromDate: date,
    //   toDate: data.toDate,
    //   trainers: data.trainer,
    //   track: [data.track]
    // }
    // this.getGreyhoundsByFilter(requestBody);

    let requestBody = {
      track: [data.track],
      fromDate: date,
      toDate: data.toDate
    }
    await this.getTrainersByFilter(requestBody);
  }

  async onToDateChange(date) {
    let data = this.formGroup.getRawValue();
    // let requestBody = {
    //   fromDate: data.fromDate,
    //   toDate: date,
    //   trainers: data.trainer,
    //   track: [data.track]
    // }
    // this.getGreyhoundsByFilter(requestBody);

    let requestBody = {
      track: [data.track],
      fromDate: data.fromDate,
      toDate: date
    }
    await this.getTrainersByFilter(requestBody);
  }

  onSelectionChangeTrainer(trainer) {
    this.isLoading = true;
    let data = this.formGroup.getRawValue();
    let trainers = "";
    trainer.forEach(element => {
      trainers += `trainer=${element}&`;
    });
    trainers = trainers.substring(0, trainers.length - 1);
    let requestBody = {
      fromDate: data.fromDate,
      toDate: data.toDate,
      trainers: trainer,
      track: [data.track]
    }
    this.getGreyhoundsByFilter(requestBody);
  }

  getGreyhoundsByFilter(requestBody: any) {
    this.isLoading = true;
    this.filteredTrainerList = [];
    this.filteredGreyhoundList = [];
    this.filteredGreyhoundList = [];
    this.greyhoundList = [];
    this._greyhoundService.getGreyhoundByDateAndTrackAndTrainers(requestBody).then((response: any) => {
      this.greyhoundList = response.body.data;
      this.filteredGreyhoundList = this.greyhoundList;
      this.filteredGreyhoundList.sort(function (a, b) {
        if (a.greyhoundName < b.greyhoundName) { return -1; }
        // if(a.firstname > b.firstname) { return 1; }
        return 0;
      })
      this.isLoading = false;
    })
  }

  onInputChangesTrack(event: any) {
    const searchString = event.target.value.toLowerCase();
    this.trackList = this.trackList.filter((obj: any) =>
      obj.name.toLowerCase().includes(searchString)
    );
  }

  onOpenChangesTrack(multiUserSearch: HTMLInputElement) {
    if (!this.formGroup.get('track')?.value) {
      multiUserSearch.value = '';
      this.trackList = this.trackList;
    }
  }

  onInputChangesTrainer(event: any) {
    const searchString = event.target.value.toLowerCase();
    this.trainerList = this.trainerList.filter((obj: any) =>
      obj.name.toLowerCase().includes(searchString)
    );
  }

  onOpenChangesTrainer(multiUserSearch: HTMLInputElement) {
    if (!this.formGroup.get('trainer')?.value) {
      multiUserSearch.value = '';
      this.trainerList = this.trainerList;
    }
  }

  onInputChangeGreyhound(event: any) {
    const searchString = event.target.value.toLowerCase();
    this.filteredGreyhoundList = this.greyhoundList.filter((obj: any) =>
      obj.name.toLowerCase().includes(searchString)
    );
    this.filteredGreyhoundList.sort(function (a, b) {
      if (a.greyhoundName < b.greyhoundName) { return -1; }
      // if(a.firstname > b.firstname) { return 1; }
      return 0;
    })
  }

  onOpenChangeGreyhound(multiUserSearch: HTMLInputElement) {
    if (!this.formGroup.get('greyhound')?.value) {
      multiUserSearch.value = '';
      this.filteredGreyhoundList = this.greyhoundList;
      this.filteredGreyhoundList.sort(function (a, b) {
        if (a.greyhoundName < b.greyhoundName) { return -1; }
        // if(a.firstname > b.firstname) { return 1; }
        return 0;
      })
    }
  }

  search() {
    this.isLoading = true;
    let data = this.formGroup.getRawValue();
    this._greyhoundService.greyhoundCompare(data).then((response: any) => {
      this.greyhoundData = response.body.data;
      this.setGraphData();
      this.isLoading = false;
    });
  }

  export() {
    this.isLoading = true;
    let data = this.formGroup.getRawValue();
    this._greyhoundService.greyhoundCompare(data).then((response: any) => {
      this.greyhoundData = response.body.data;
      this._greyhoundService.exportGreyhoundCompare(this.greyhoundData);
      this.isLoading = false;
    });
  }

  setGraphData() {
    let data = this.formGroup.getRawValue();
    let chartDataList: any = [];
    let xacesData: any[] = [];
    this.yacesData = [];
    this.greyhoundData.forEach(element => {
      xacesData.push(element.greyhoundName);
      if (data.chartKind === 'rating') {
        this.yacesData.push(Math.round(element.rating * 100) / 100);
      } else if (data.chartKind === 'stmhcp') {
        this.yacesData.push(Math.round(element.stmhcp * 100) / 100);
      } else if (data.chartKind === 'wintime') {
        this.yacesData.push(Math.round(element.winTime * 100) / 100);
      } else if (data.chartKind === 'calctm') {
        this.yacesData.push(Math.round(element.calcTime * 100) / 100);
      }
    });
    var randomColor = "#" + Math.floor(Math.random() * 16777215).toString(16);
    chartDataList.push(
      {
        label: data.chartKind,
        data: this.yacesData,
        borderWidth: 0,
        fill: true,
        backgroundColor: [randomColor]
        // steppedLine: true
      }
    );
    this.lineChartSteppedData = chartDataList;
    this.lineChartLabels = xacesData;

    this.chartOptions = {
      series: [
        {
          name: data.chartKind,
          data: this.yacesData
        }
      ],
      chart: {
        height: 350,
        type: "bar"
      },
      title: {
        text: ""
      },
      xaxis: {
        categories: this.lineChartLabels
      }
    };
  }

  getAllTracks() {
    this.isLoading = true
    this._greyhoundService.getTracks().then((response: any) => {
      this.trackList = response.body.data;
      this.trackList.sort(function (a, b) {
        if (a.name < b.name) { return -1; }
        // if(a.firstname > b.firstname) { return 1; }
        return 0;
      })
      this.isLoading = false
    });
  }

  drop(event: CdkDragDrop<string[]>) {
    if (this.selectedNames.length <= 5) {
      if (event.previousContainer === event.container) {
        moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
      } else {
        transferArrayItem(
          event.previousContainer.data,
          event.container.data,
          event.previousIndex,
          event.currentIndex,
        );
      }
      this.shftesVar = event.container.data
      this.greyhoundIds = [];
      for (let element of this.shftesVar) {
        let ids: any = element.greyhoundId
        this.greyhoundIds.push(ids)
      }
      this.formGroup.controls.greyhound.setValue(this.greyhoundIds)
      // this.getCharts();
    } else {
      this._utilityService.successMessage("Six item allowed to compare", "ERROR");
    }
  }

  refreshDropList() {
    this.selectedNames = []
    this.formGroup.controls.greyhound.setValue(null);
  }

  reset() {
    this.formGroup.reset();
    this.formGroup.controls.distance.setValue('0');
    this.trainerList = [];
    this.greyhoundIds = [];
    this.filteredGreyhoundList = [];
    this.refreshDropList();
    this.lineChartSteppedData = [{
      data: [],
      label: '',
      borderWidth: 0,
      fill: true,
      backgroundColor: ["navy", "black", "navy"],
      hoverBackgroundColor: ["darknavy", "darkblack", "darknavy"],
      // steppedLine: true
    }];
  }

  onChangeSearch(value: any) {
    this.filteredGreyhoundList = this.greyhoundList.filter(item => item.greyhoundName.toLowerCase().includes(value.toLowerCase()));
    this.filteredGreyhoundList.sort(function (a, b) {
      if (a.greyhoundName < b.greyhoundName) { return -1; }
      // if(a.firstname > b.firstname) { return 1; }
      return 0;
    })
  }
}
