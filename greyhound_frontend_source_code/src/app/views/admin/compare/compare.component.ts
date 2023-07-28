import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ChartConfiguration, ChartData } from 'chart.js';
import { CompareService } from './compare.service';

@Component({
  selector: 'app-compare',
  templateUrl: './compare.component.html',
  styleUrls: ['./compare.component.scss']
})
export class CompareComponent implements OnInit {
  formGroup!: FormGroup;
  isLoading:boolean=false;
  chartKindData:any[]=["weight","calctm","stmhcp","position"];
  greyhoundNames:any[]=[];
  selectedNames:any=[]
  shftesVar = []
  
  constructor(
    private _compareService : CompareService
  ) { }

  ngOnInit(): void {
    this.formGroup = this._compareService.createFilterForm(null);
    this.searchByName()
  }

  drop(event: CdkDragDrop<string[]>) {
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
    console.log('>>>>>',this.shftesVar);
    
  
  }
  // public barChartLegend = true;
  // public barChartPlugins = [];

  // public barChartData: ChartConfiguration<'bar'>['data'] = {
  //   labels: [ '2006', '2007', '2008', '2009', '2010', '2011', '2012' ],
  //   datasets: [
  //     { data: [ 65, 59, 80, 81, 56, 55, 40 ], label: 'Series A' },
  //     { data: [ 28, 48, 40, 19, 86, 27, 90 ], label: 'Series B' }
  //   ]
  // };

  // public barChartOptions: ChartConfiguration<'bar'>['options'] = {
  //   responsive: true,
  // };

  searchByName(){
    this.isLoading=true
    let data= this.formGroup.getRawValue()
    this._compareService.getGreyhoundSearchByName().then((response:any)=>{
      this.greyhoundNames= response.body.data
      this.isLoading=false;
    })
    
  }

    lineChartSteppedData: Array <any> = [{
    data: [],
    label: 'Order',
    borderWidth: 0,
    fill: true,
    backgroundColor: ["navy", "black","navy"],
        hoverBackgroundColor: ["darknavy", "darkblack", "darknavy"],
    // steppedLine: true
  }];
  public lineChartLabels: Array<any> = [];
  /*
  * Full width Chart Options
  */
  async getCharts(){
    let data= this.formGroup.getRawValue()
    let data1
    let employeeIds = []
    employeeIds.push(...this.shftesVar.map((emp: { id: any; }) => emp.id));
    console.log(employeeIds);
    
    this._compareService.getGreyhoundReport(data.chartKind).then((response:any)=>{
      data1 = response.body.data;
      let ids = Object.keys(data1)[0]
      let idDataArr = data1[ids]
      let iddata = []

      for (let dateData of Object.keys(data1[ids])){
        iddata.push(idDataArr[dateData])
      }
      this.lineChartSteppedData = [{
        data: iddata,
        label: ids,
        borderWidth: 0,
        fill: true,
        backgroundColor: ["navy", "black","navy"],
            hoverBackgroundColor: ["darknavy", "darkblack", "darknavy"],
        // steppedLine: true
      }];
      this.lineChartLabels = Object.keys(data1[ids])

      
    })
   
  }
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
  public lineChartLegend: boolean = false;
  public lineChartType: string = 'line';
  // public lineChartData: ChartData<'bar'> = {
  //   datasets: [
  //     {
  //       backgroundColor: ["red", "green", "blue"],
  //       hoverBackgroundColor: ["darkred", "darkgreen", "darkblue"],
  //     }
  //   ]
  // };

}
