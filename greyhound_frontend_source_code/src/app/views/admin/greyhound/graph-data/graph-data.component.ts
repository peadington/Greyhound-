import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { id } from 'date-fns/locale';
import { CompareService } from '../../compare/compare.service';

@Component({
  selector: 'app-graph-data',
  templateUrl: './graph-data.component.html',
  styleUrls: ['./graph-data.component.scss']
})
export class GraphDataComponent {
  formGroup!: FormGroup;
  isLoading:boolean=false;
  chartKindData:any[]=["distance","weight","calctm","stmhcp","position"];
  greyhoundNames:any[]=[];
  selectedNames:any=[]
  shftesVar:any[] = [];
  greyhoundId:any[]
  greyhoundIds:any[]=[];
  greyhoundName:any;
  minDate: Date = new Date();
  
  constructor(
    private _compareService : CompareService,
    private _activatedRoute: ActivatedRoute
  ) {this.greyhoundId = this._activatedRoute.snapshot.params['id'];
  this.greyhoundName = this._activatedRoute.snapshot.params['name']
   }

  ngOnInit(): void {
    this.formGroup = this._compareService.createFilterForm(null);     
    this.greyhoundIds.push(this.greyhoundId)
    this.getCharts();
    this.searchByName();
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
    let data= this.formGroup.getRawValue();
    let graphData= {
      "chartKind": data.chartKind,
      "fromDate": new Date(data.fromDate),
      "greyhoundIds": this.greyhoundIds,
      "toDate": new Date(data.toDate)
    }
    this._compareService.getGreyhoundReport(graphData).then((response:any)=>{
      let graphDataResponse = response.body.data;
      let ids:any[] = Object.keys(graphDataResponse);
      let chartDataList:any = [];
      ids.forEach(id => {
        let dates = Object.keys(graphDataResponse[id]);
        let dataList:any[] = [];
        dates.forEach(date => {
          dataList.push(graphDataResponse[id][date]);
        });
        var randomColor = "#" + Math.floor(Math.random()*16777215).toString(16);
        chartDataList.push(
          {
            data: dataList,
            label: id,
            borderWidth: 0,
            fill: true,
            backgroundColor: [randomColor]
            // steppedLine: true
          }
        );
      });
      this.lineChartSteppedData = chartDataList;
      this.lineChartLabels = Object.keys(graphDataResponse[Object.keys(graphDataResponse)[0]]);
      // let iddata = [];

      // for (let dateData of Object.keys(data1[ids])){
      //   iddata.push(idDataArr[dateData])
      // }
      // this.lineChartSteppedData = [{
      //   data: iddata,
      //   label: ids,
      //   borderWidth: 0,
      //   fill: true,
      //   backgroundColor: ["grey", "black",],
      //       hoverBackgroundColor: ["darkgrey", "darkblack", ],
      //   // steppedLine: true
      // }];
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
    this.greyhoundIds=[];
    for(let element of this.shftesVar){
      let ids:any= element.greyhoundId
      this.greyhoundIds.push(ids)
    }
    console.log("dddd", this.greyhoundId);
    this.getCharts();
  }

  searchByName(){
    this.isLoading=true
    this._compareService.getGreyhoundSearchByName().then((response:any)=>{
      this.greyhoundNames= response.body.data
      this.isLoading=false;
      this.shftesVar.push(this.greyhoundNames.find(item=>item.greyhoundId === this.greyhoundId));
    })
    
  }
}
