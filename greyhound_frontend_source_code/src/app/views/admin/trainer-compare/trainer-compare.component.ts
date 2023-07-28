import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { UtilityService } from '../../../shared/services/utility.service';
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
  selector: 'app-trainer-compare',
  templateUrl: './trainer-compare.component.html',
  styleUrls: ['./trainer-compare.component.scss']
})
export class TrainerCompareComponent {
  formGroup!: FormGroup;
  isLoading: boolean = false;
  displayedColumns: any = [];
  displayedTrainerColumns: any[] = [];
  whenData: any[] = [
    { key: 7, value: "7 days" }, { key: 30, value: "30 days" }, { key: 120, value: "120 days" }, { key: 365, value: "1 Year" }, { key: 1, value: "Life Time" }];
  distanceData: any[] = [245, 261, 290, 435, 450, 480, 590, 640, 706];
  chartKindData: any[] = [
    { key: "By Rating", value: 'rating' },
    { key: "By STmHcp", value: 'stmhcp' },
    { key: "By WinTime", value: 'wintime' },
    { key: "By CalcTm", value: 'calctm' },
    { key: "By Total Income", value: 'income' },
    { key: "By Wins against runs", value: 'wins' }
  ];
  trackList: any[] = [];
  filterValue: any;
  trainerData: any;
  trainerList: any[] = [];
  filteredTrainerList: any[] = [];
  shftesVar: any[] = []
  trainerIds: any;
  selectedNames: any[] = []

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
    maintainAspectRatio: true,
    legend: {
      display: true,
      position: 'bottom'
    },
    scales: {
      xAxes: [{
        display: true,
        gridLines: {
          color: 'rgba(0,0,0,0.02)',
          zeroLineColor: 'rgba(0,0,0,0.02)'
        }
      }],
      yAxes: [{
        display: true,
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

  @ViewChild("chart") chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;
  winList: any[] = [];
  prizeList: any[][];

  constructor(
    private _reportService: ReportService,
    private _greyhoundService: GreyhoundService,
    public _utilityService: UtilityService,
    private _trainerService: TrainerService
  ) { }

  ngOnInit(): void {
    this.formGroup = this._trainerService.createTrainerCompareFilterForm();
    this.displayedColumns = this._reportService.displayedColumns;
    this.displayedTrainerColumns = this._reportService.displayedTrainerColumns;
    this.getAllTracks();
  }

  onSelectionChangeTrack(track) {
    const data = this.formGroup.getRawValue();
    let tracks = `track=${track}`;
    this.isLoading = true;
    let requestBody = {
      track: [track],
      fromDate: data.fromDate,
      toDate: data.toDate
    }
    this.getTrainersByFilter(requestBody);
  }

  onFromDateChange(date) {
    let data = this.formGroup.getRawValue();
    let requestBody = {
      track: [data.track],
      fromDate: date,
      toDate: data.toDate
    }
    this.getTrainersByFilter(requestBody);
  }

  onToDateChange(date) {
    let data = this.formGroup.getRawValue();
    let requestBody = {
      track: [data.track],
      fromDate: data.fromDate,
      toDate: date
    }
    this.getTrainersByFilter(requestBody);
  }

  getTrainersByFilter(requestBody: any) {
    this.isLoading = true;
    this.trainerList = [];
    this.filteredTrainerList = [];
    this._trainerService.getTrainerListByDateAndTrack(requestBody).then((response: any) => {
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

  search() {
    this.isLoading = true;
    let data = this.formGroup.getRawValue();
    this._trainerService.trainerCompare(data).then((response: any) => {
      this.trainerData = response.body.data;
      this.setGraphData();
      this.isLoading = false;
    });
  }

  public wins: string = "Wins";
  public prize: string = "Prize Money in £";
  setGraphData() {
    let graphData: any[] = Object.values(this.trainerData);
    this.winList = [];
    this.prizeList = [];
    let chartDataList: any = [];
    graphData.forEach(element => {
      this.winList.push(element.win)
      this.prizeList.push(element.prize)
    });
    var randomColor = "#" + Math.floor(Math.random() * 16777215).toString(16);
    var randomColor1 = "#" + Math.floor(Math.random() * 16755215).toString(16);
    chartDataList.push(
      {
        label: this.wins,
        data: this.winList,
        borderWidth: 0,
        fill: true,
        backgroundColor: [randomColor1],
        steppedLine: true
      }
    );
    chartDataList.push(
      {
        label: this.prize,
        data: this.prizeList,
        borderWidth: 0,
        fill: true,
        backgroundColor: [randomColor],
        steppedLine: true
      }
    );
    this.lineChartSteppedData = chartDataList;
    this.lineChartLabels = Object.keys(this.trainerData);

    this.chartOptions = {
      series: [
        {
          name: "Win",
          data: this.winList
        },
        {
          name: "Prize",
          data: this.prizeList
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

  export() {
    this.isLoading = true;
    let data = this.formGroup.getRawValue();
    this._trainerService.trainerCompare(data).then((response: any) => {
      this.trainerData = response.body.data;
      this._trainerService.exportTrainerCompare(this.trainerData);
      this.isLoading = false;
    });
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
      this.trainerIds = [];
      for (let element of this.shftesVar) {
        let ids: any = element.id
        this.trainerIds.push(ids)
      }
      this.formGroup.controls.trainer.setValue(this.trainerIds)
      // this.getCharts();
    } else {
      this._utilityService.successMessage("Six item allowed to compare", "ERROR");
    }
  }

  refreshDropList() {
    this.selectedNames = []
    this.formGroup.controls.trainer.setValue(null)
  }

  reset() {
    this.formGroup.reset();
    this.trainerList = [];
    this.filteredTrainerList = [];
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
    this.filteredTrainerList = this.trainerList.filter(item => item.name.toLowerCase().includes(value.toLowerCase()));
    this.filteredTrainerList.sort(function (a, b) {
      if (a.name < b.name) { return -1; }
      // if(a.firstname > b.firstname) { return 1; }
      return 0;
    })
  }
}
