<template>
  <div>
    <header id="headers">
     <div style="padding: 8px 0 8px 0 ;margin-left: 600px">
       <el-input size="small"  @keyup.enter.native="clickSearch" v-model="searchtext" placeholder="请输入内容...例如java" style="width: 400px " ></el-input>
     </div>
    </header>
    <div id="gid_tc" style="float:left; background-color: #dedede">

      <div id="gid">
        <div style="padding-top: 20px">
          <el-button type="primary" icon="el-icon-circle-plus" style="margin-left: 800px" @click="addOneNodeButton">添加一个节点</el-button>
        </div>
      </div>
      <div class="mengceng"></div>
    </div>
    <div class="svg-set-box clearfix">
      <div class="ctwh-dibmr" style="float: right">
        <ul class="toolbar">
          <li>
            <a href="javascript:;" @click="zoomIn"
              ><span><i class="el-icon-zoom-in"></i>放大</span></a
            >
          </li>
          <li>
            <a href="javascript:;" @click="zoomOut"
              ><span><i class="el-icon-zoom-out"></i>缩小</span></a
            >
          </li>
          <li>
            <a href="javascript:;" @click="refresh"
              ><span><i class="el-icon-refresh-right"></i>还原</span></a
            >
          </li>
          <li>
            <a
              v-if="!isFullscreen"
              id="fullscreenbtn"
              href="javascript:;"
              @click="showFull"
            >
              <span><i class="el-icon-full-screen"></i>全屏</span>
            </a>
            <a
              v-else
              id="fullscreenbtn"
              href="javascript:;"
              @click="exitFullScreen"
            >
              <span><i class="el-icon-full-screen"></i>退出</span>
            </a>
          </li>
        </ul>
      </div>
    </div>
    <el-dialog
            title="添加节点"
            :visible.sync="dialogVisible"
            width="30%">
      节点名称：<el-input size="small" v-model="addInfo.ename" placeholder="请输入节点名字" style="width: 300px"></el-input>
      <br>
      <br>
      <p>图片上传：</p>
      <br>
<!--      图片：<el-input size="small" v-model="addInfo.imgurl" placeholder="请输入节点图片url" style="width: 300px"></el-input>-->
      <el-upload
              class="upload-demo"
              drag
              action="/oss/uploadfile"
              :on-success="uploadSuccessHandler">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过10mb</div>
      </el-upload>
      <br>
      <br>
      属性：<el-input size="small" v-model="addInfo.rname" placeholder="请输入关系" style="width: 300px"></el-input>

      <span slot="footer" class="dialog-footer">

    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary"  @click="addRequest">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
            title="编辑数据"
            :visible.sync="EditdialogVisible"
            width="30%">

      节点名称：<el-input size="small" v-model="editInfo.ename" placeholder="请输入修改的节点名字" style="width: 300px"></el-input>
      <br>
      <br>
<!--      图片：<el-input size="small" v-model="editInfo.imgurl" placeholder="请输入修改的节点图片url" style="width: 300px"></el-input>-->
      <p>请上传要修改的图片：</p>
      <br>
      <el-upload
              class="upload-demo"
              drag
              action="/oss/uploadfile"
              :on-success="uploadEditSuccessHandler">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过10mb</div>
      </el-upload>
      <br>
      <br>
      属性：<el-input size="small" v-model="editInfo.rname" placeholder="请输入要修改的关系" style="width: 300px"></el-input>
      <span slot="footer" class="dialog-footer">
    <el-button @click="EditdialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="editRequest">确 定</el-button>
  </span>
    </el-dialog>

    <el-dialog
            title="添加一个新节点"
            :visible.sync="addOneNodeBound"
            width="30%">

      节点名称：<el-input size="small" v-model="addNewNode.name" placeholder="请输入要添加的节点名字" style="width: 300px"></el-input>
      <br>
      <br>
      图片：<el-input size="small" v-model="addNewNode.imgsrc" placeholder="请输入修改的节点图片url" style="width: 300px"></el-input>
      <br>
      <br>
      <span slot="footer" class="dialog-footer">
    <el-button @click="EditdialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="addOneNode">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>
<script>
import * as d3 from "d3";
import $ from "jquery";
import {Message} from "element-ui";
import {deleteRequest} from "../utils/api";
export default {
  props: ["pid"],
  data() {
    return {
      searchtext: "",
      dialogVisible: false,        //追加框是否可见
      EditdialogVisible: false,    //编辑框是否可见
      addOneNodeBound: false,      //添加一个节点是否可见

      deleteInfo: {
        id: "",
        name: ""
      },
      addNewNode: {
        name: "",
        imgsrc: ""
      },
      addInfo: {
        rname: "",
        imgurl: "",
        sourceId: "",
        ename: ""
      },
      editInfo: {
        ename: "",
        imgurl: "",
        rname: "",
        sourceId: ""
      },
      theme: 0,
      loading: false,
      width: 1000,
      height: 800,
      gcontainer: {},
      svg: {},
      zoom: null,
      arrowMarker: {},
      simulation: {},
      isFullscreen: false,
      qaGraphNode: {},
      qaButtonGroup: {},
      qaGraphNodeText: {},
      qaGraphLink: {},
      qaGraphLinkText: {},
      graph: {
        nodes: [],
        links: []
      },
      defaultR: 30,
      colorList: [
        "#ff8373",
        "#f9c62c",
        "#a5ca34",
        "#6fce7a",
        "#70d3bd",
        "#ea91b0"
      ],
      toolbarData: [
        { name: "编辑", value: 1, code: "edit" },
        { name: "追加", value: 1, code: "append" },
        { name: "删除", value: 1, code: "delete" }
      ],
      selectUuid: 0,
      selectName: "",
      selectImg: "",
      selectRelation: "",
      nodeRecordList: []
    };
  },
  components: {},
  mounted() {
    this.initGraphContainer();
    this.addMaker();
    // this.initGraph();
  },
  created() {},
  watch: {},
  methods: {
    uploadEditSuccessHandler(resp){
      this.editInfo.imgurl = resp.data.url;
    },
    uploadSuccessHandler(resp){
      this.addInfo.imgurl = resp.data.url;
    },
    addOneNodeButton(){
      var _this = this;
      _this.addOneNodeBound = true;
    },
    addOneNode(){
      var _this = this;
      _this.postRequest("/entity/addOne",_this.addNewNode).then(resp=>{
        if (resp){
          _this.addOneNodeBound = false;
          if(resp.data.code==200){
            Message.success("添加成功")
          }
          _this.searchtext = _this.addNewNode.name;
          this.initGraph();
        }
        else{
          _this.addOneNodeBound = false;
          Message.error("添加失败")
        }
      })
    },
    editRequest(){
      var _this = this
      _this.editInfo.sourceId = this.selectUuid+'';
      console.log(_this.editInfo)
      _this.putRequest("/entity/update",_this.editInfo).then(resp=>{
        if (resp){
          this.EditdialogVisible = false;
          this.initGraph();
        }
      })
    },
    addRequest(){
      var _this = this
      this.addInfo.sourceId = this.selectUuid+'';
      console.log(this.addInfo)
      _this.postRequest("/entity/add", this.addInfo).then(resp => {
        if (resp){
        this.dialogVisible = false;
        if(resp.data.code==200){
          Message.success("添加成功")
        }
          _this.addInfo.ename = "";
          _this.addInfo.rname = "";
          _this.addInfo.imgurl = "";

          this.initGraph();
        }
      })
    },
    clickSearch(){
      this.initGraph();
      Message.success("查询成功");
    },
    initGraphContainer() {
      this.gcontainer = d3.select("#gid");
      if (this.isFullscreen) {
        this.width = window.screen.width;
        this.height = window.screen.height;
      } else {
        this.width = $("#" + this.pid).width();
        this.height = $("#" + this.pid).height();
      }
      this.svg = this.gcontainer.append("svg");
      var sWidth = this.width;
      var sHeight = this.height;
      this.svg.attr("width", sWidth);
      this.svg.attr("height", sHeight);
      // this.svg.attr("viewBox", "0 0 " + sWidth / 2 + " " + sHeight / 2);
      this.svg.attr("id", "svg_idx");
      this.svg.attr("preserveAspectRatio", "xMidYMidmeet");
      this.simulation = d3
        .forceSimulation()
        .force("charge", d3.forceManyBody().strength(-1500))
        .force(
          "link",
          d3
            .forceLink()
            .distance(60)
            .id(function(d) {
              return d.uuid;
            })
        )
        .force("collide", d3.forceCollide().strength(-30))
        .force("center", d3.forceCenter(this.width / 2, this.height / 2));
      this.qaGraphLink = this.svg.append("g").attr("class", "line");
      this.qaGraphLinkText = this.svg.append("g").attr("class", "linetext");
      this.qaGraphNode = this.svg.append("g").attr("class", "node");
      this.qaGraphNodeText = this.svg.append("g").attr("class", "nodetext");
      this.nodebuttonGroup = this.svg.append("g").attr("class", "nodebutton");
      this.svg.on(
        "click",
        function() {
          d3.selectAll(".buttongroup").classed("notshow", true);
        },
        false
      );
    },
    initGraph() {
      var _this = this;
      console.log(_this.searchtext)
        _this.getRequest("/entity/" + _this.searchtext)
        .then(function(response) {
          var data = response.data.data;
          console.log(data)
          _this.graph.nodes = data.node;
          _this.graph.links = data.relationship;
          _this.updateGraph();
        });
    },
    addMaker() {
      var arrowMarker = this.svg
        .append("marker")
        .attr("id", "arrow")
        .attr("markerUnits", "strokeWidth")
        .attr("markerWidth", "12") //
        .attr("markerHeight", "12")
        .attr("viewBox", "0 0 12 12")
        .attr("refX", "38")
        .attr("refY", "6")
        .attr("orient", "auto");
      var arrowPath = "M2,2 L10,6 L2,10 L6,6 L2,2"; // 定义箭头形状
      arrowMarker
        .append("path")
        .attr("d", arrowPath)
        .attr("fill", "#cc260e");
    },
    drawNode(nodes) {
      var _this = this;
      var node = this.qaGraphNode.selectAll("circle").data(nodes, function(d) {
        return d.uuid;
      });
      node.exit().remove();
      var nodeEnter = node.enter().append("circle");
      nodeEnter.on("click", function(d) {
        console.log("触发单击");
        console.log(d)
        console.log(d.uuid)
        console.log(d.name)
        console.log(d.imgsrc)
        _this.selectUuid = d.uuid;   //保存uuid
        _this.selectName = d.name;   //保存name
        _this.selectImg = d.imgsrc;  //保存img
        var out_buttongroup_id = ".out_buttongroup_" + d.uuid;
        var selectItem = d3.select(out_buttongroup_id)._groups[0][0];
        if (selectItem.classList.contains("notshow")) {
          _this.svg.selectAll(".buttongroup").classed("notshow", true);
          d3.select(out_buttongroup_id).classed("notshow", false);
        } else {
          d3.select(out_buttongroup_id).classed("notshow", true);
        }
        event.stopPropagation();
      });
      nodeEnter.on("dblclick", function(d) {
        console.log("触发双击:" + d);
        event.preventDefault();
      });
      nodeEnter.on("mouseenter", function() {
        console.log("鼠标移入");
        d3.select(this).style("stroke-width", "6");
      });
      nodeEnter.on("mouseleave", function() {
        console.log("鼠标移出");
        d3.select(this).style("stroke-width", 2);
        //todo其他节点和连线一并显示
        d3.select(".node").style("fill-opacity", 1);
        d3.select(".nodetext").style("fill-opacity", 1);
        d3.selectAll(".line").style("stroke-opacity", 1);
        d3.selectAll(".linetext").style("fill-opacity", 1);
      });
      nodeEnter.on("mouseover", function(d) {
        //todo鼠标放上去只显示相关节点，其他节点和连线隐藏
        d3.selectAll(".node").style("fill-opacity", 0.2);
        var relvantNodeIds = [];
        var relvantNodes = _this.graph.links.filter(function(n) {
          return n.sourceid == d.uuid || n.targetid == d.uuid;
        });
        relvantNodes.forEach(function(item) {
          relvantNodeIds.push(item.sourceid);
          relvantNodeIds.push(item.targetid);
        });
        //显示相关的节点
        _this.qaGraphNode
          .selectAll("circle")
          .style("fill-opacity", function(c) {
            if (relvantNodeIds.indexOf(c.uuid) > -1) {
              return 1.0;
            }
          });
        //透明所有节点文字
        d3.selectAll(".nodetext").style("fill-opacity", 0.1);

        //显示相关的节点文字
        _this.qaGraphNodeText
          .selectAll("text")
          .style("fill-opacity", function(c) {
            if (relvantNodeIds.indexOf(c.uuid) > -1) {
              return 1.0;
            }
          });
        //透明所有连线
        d3.selectAll(".line").style("stroke-opacity", 0.5);
        //显示相关的连线
        _this.qaGraphLink
          .selectAll("line")
          .style("stroke-opacity", function(c) {

            if (c.lk.targetid === d.uuid) {
              return 1.0;
            }
          });
        //透明所有连线文字
        d3.selectAll(".linetext").style("fill-opacity", 0.5);
        //显示相关的连线文字
        _this.qaGraphLinkText
          .selectAll(".linetext")
          .style("fill-opacity", function(c) {
            if (c.lk.targetid === d.uuid) {
              console.log("连线文字显示")
              _this.selectRelation = c.lk.name;
              return 1.0;
            }
          });
      });
      nodeEnter.call(
        d3
          .drag()
          .on("start", _this.dragStarted)
          .on("drag", _this.dragged)
          .on("end", _this.dragEnded)
      );
      node = nodeEnter.merge(node).text(function(d) {
        return d.name;
      });
      node.style("stroke", function(d) {
        if (d.color) {
          return d.color;
        }
        return "#58edb5";
      });
      node.style("stroke-opacity", 0.6);
      node.attr("r", function(d) {
        if (d.r) {
          return d.r;
        }
        return d.index === 0 ? 28 : 20;
      });
      node.attr("fill", function(d, i) {
        //创建圆形图像
        if (d.imgsrc) {
          console.log(d.imgsrc)
          var img_w = 77,
            img_h = 80;
          var defs = d3.selectAll("svg >defs");
          console.log("=============")
          console.log(d.r)
          console.log("=============")
          var catpattern = defs
            .append("pattern")
            .attr("id", "catpattern" + i)
            .attr("height", 1)
            .attr("width", 1);
          catpattern
            .append("image")
            .attr("x", -(img_w / 2 - d.r))
            .attr("y", -(img_h / 2 - d.r))
            .attr("width", img_w)
            .attr("height", img_h)
            .attr("xlink:href", d.imgsrc);

          return "url(#catpattern" + i + ")";
        } else {
          if (d.cur === "1") {
            return _this.colorList[0];
          } else {
            return _this.colorList[2];
          }
        }
      });
      node
        .append("title") // 为每个节点设置title
        .text(function(d) {
          if (d.name) {
            return d.name;
          }
          return "";
        });
      return node;
    },
    drawNodeText(nodes) {
      var _this = this;
      var nodetext = this.qaGraphNodeText
        .selectAll("text")
        .data(nodes, function(d) {
          return d.uuid;
        });
      nodetext.exit().remove();
      var nodetextEnter = nodetext.enter().append("text");
      nodetextEnter.call(
        d3
          .drag()
          .on("start", _this.dragStarted)
          .on("drag", _this.dragged)
          .on("end", _this.dragEnded)
      );
      nodetext = nodetextEnter.merge(nodetext).text(function(d) {
        return d.name;
      });
      nodetext
        .style("fill", function() {
          return "#333";
        })
        .attr("class", "nodetext")
        .attr("dy", "3.6em")
        .attr("font-family", "楷体")
        .attr("font-size", 16)
        .attr("text-anchor", "middle")
        .text(function(d) {
          return d.name;
        });
      nodetext
        .append("title") // 为每个节点设置title
        .text(function(d) {
          if (d.name) {
            return d.name;
          }
          return "";
        });
      return nodetext;
    },
    drawLink(links) {
      var _this = this;
      var link = this.qaGraphLink.selectAll("line").data(links, function(d) {
        return d.uuid;
      });
      link.exit().remove();
      var linkEnter = link
        .enter()
        .append("line")
        .attr("class", "link")
        .attr("stroke-width", 1)
        .attr("stroke", function() {
          return _this.colorList[5];
        })
        .attr("marker-end", "url(#arrow)"); // 箭头
      link = linkEnter.merge(link);
      return link;
    },
    drawLinkText(links) {
      var _this = this;
      var linktext = _this.qaGraphLinkText
        .selectAll("text")
        .data(links, function(d) {
          return d.uuid;
        });
      console.log(linktext)
      console.log(_this.qaGraphLinkText)
      linktext.exit().remove();
      var linktextEnter = linktext
        .enter()
        .append("text")
        .attr("class", "linetext")
        .style("fill", "#875034")
        .style("font-size", "15px")
        .text(function(d) {
          return d.lk.name;
        });
      linktext = linktextEnter.merge(linktext).text(function(d) {
        return d.lk.name;
      });
      return linktext;
    },
    drawButtonGroup(nodes) {
      var _this = this;
      d3.selectAll(".nodebutton >g").remove();
      var nodebutton = _this.nodebuttonGroup
        .selectAll(".nodebutton")
        .data(nodes, function(d) {
          return d.uuid;
        });
      nodebutton.exit().remove();
      var nodebuttonEnter = nodebutton
        .enter()
        .append("use") //  为每个节点组添加一个 use 子元素
        .attr("r", function(d) {
          if (!d.r) {
            return _this.defaultR;
          }
          return d.r;
        })
        .attr("uuid", function(d) {
          return d.uuid;
        })
        .attr("xlink:href", function(d) {
          if (!d.r) {
            return "#out_circle_" + _this.defaultR;
          }
          return "#out_circle_" + d.r;
        }) //  指定 use 引用的内容
        .attr("class", function(d) {
          return "buttongroup out_buttongroup_" + d.uuid;
        })
        .classed("notshow", true);
      nodebutton = nodebuttonEnter.merge(nodebutton);
      return nodebutton;
    },
    drawToolButton() {
      var _this = this;
      //先删除所有为节点自定义的按钮组
      d3.selectAll("svg >defs").remove();
      var nodes = _this.graph.nodes;
      var pie = d3.pie().value(function(d) {
        return d.value; //处理数据，指定value作为计算比例的字段
      });
      var piedata = pie(_this.toolbarData);
      var nodeButtonGroup = this.svg.append("defs");
      var nodeRArr = [];
      nodes.forEach(function(m) {
        if (!m.r) {
          m.r = _this.defaultR;
        }
        //按半径分别定义每种按钮组的图标
        if (nodeRArr.indexOf(m.r) == -1) {
          nodeRArr.push(m.r);
          var nbtng = nodeButtonGroup
            .append("g")
            .attr("id", "out_circle_" + m.r); //为每一个节点定制一个按钮组，在画按钮组的时候为其指定该id
          var buttonGroupEnter = nbtng
            .selectAll(".buttongroup")
            .data(piedata)
            .enter()
            .append("g")
            .attr("class", function(d) {
              return "action_" + d.data.code;
            });
          var arc = d3
            .arc()
            .innerRadius(m.r)
            .outerRadius(m.r + 30);
          buttonGroupEnter
            .append("path")
            .attr("d", function(d) {
              return arc(d);
            })
            .attr("fill", "#E6A23C")
            .style("opacity", 0.6)
            .attr("stroke", "#6CB7ED")
            .attr("stroke-width", 1);
          buttonGroupEnter
            .append("text")
            .attr("transform", function(d) {
              return "translate(" + arc.centroid(d) + ")";
            })
            .attr("text-anchor", "middle")
            .text(function(d) {
              return d.data.name;
            })
            .style("fill", function() {
              return "#fff";
            })
            .attr("font-size", 10);
        }
      });
    },
    bindEventButtonGroup() {
      var _this = this;
      //按钮组事件绑定
      _this.toolbarData.forEach(function(m) {
        var btnClass = ".action_" + m.code;
        _this.svg.selectAll(btnClass).on("click", function(d) {
          console.log(d.data)
          if (d.data.name=='删除'){
            _this.deleteInfo.id = _this.selectUuid + "";
            _this.deleteInfo.name = _this.selectRelation;
            deleteRequest("/entity/delete/",_this.deleteInfo)
                    .then(resp=>{
                      if (resp){
                        if (resp.data.code==200){
                          Message.success("删除成功")
                        }
                        _this.initGraph();
                      }
                    })
          }else if(d.data.name=='编辑'){
            _this.editInfo.ename = _this.selectName;
            _this.editInfo.imgurl = _this.selectImg;
            _this.editInfo.rname = _this.selectRelation;
            console.log()
            // _this.editInfo.rname = _this.qaGraphNodeText
            _this.EditdialogVisible = true;
            _this.addInfo.sourceId = _this.selectUuid;
          } else if(d.data.name=='追加'){
            _this.dialogVisible = true;
            _this.addInfo.sourceId = _this.selectUuid;
          }
        });
      });
    },
    formatData() {
      var _this = this;
      var lks = _this.graph.links;
      var nodes = _this.graph.nodes;
      nodes.forEach(function(n) {
        if (n.center === 1 || n.center === "1") {
          n.fx = _this.width / 2;
          n.fy = _this.height / 2;
        }
        if (typeof n.fx === "undefined" || n.fx === "") {
          n.fx = null;
        }
        if (typeof n.fy === "undefined" || n.fy === "") {
          n.fy = null;
        }
      });
      var links = [];
      lks.forEach(function(m) {
        var sourceNode = nodes.filter(function(n) {
          return n.uuid === m.sourceid;
        })[0];
        if (!sourceNode) return;
        var targetNode = nodes.filter(function(n) {
          return n.uuid === m.targetid;
        })[0];
        if (!targetNode) return;
        links.push({ source: sourceNode.uuid, target: targetNode.uuid, lk: m });
      });
      var data = {};
      data.nodes = nodes;
      data.links = links;
      return data;
    },
    updateGraph() {
      var _this = this;
      var data = _this.formatData();
      var nodes = data.nodes;
      var links = data.links;
      //定义按钮组引用的use元素
      _this.drawToolButton(nodes);
      // 更新节点
      var graphNode = _this.drawNode(nodes);
      // 更新节点文字
      var graphNodeText = _this.drawNodeText(nodes);
      // 更新按钮组
      var graphNodeButtonGroup = _this.drawButtonGroup(nodes);
      // 更新连线 links
      var graphLink = _this.drawLink(links);
      // 更新连线文字
      var graphLinkText = _this.drawLinkText(links);
      _this.simulation
        .nodes(nodes)
        .alphaTarget(0)
        .alphaDecay(0.05)
        .on("tick", ticked);
      function ticked() {
        // 更新连线坐标
        graphLink
          .attr("x1", function(d) {
            return d.source.x;
          })
          .attr("y1", function(d) {
            return d.source.y;
          })
          .attr("x2", function(d) {
            return d.target.x;
          })
          .attr("y2", function(d) {
            return d.target.y;
          });
        // 刷新连接线上的文字位置
        graphLinkText
          .attr("x", function(d) {
            if (!d.source.x || !d.target.x) return 0;
            var x = (parseFloat(d.source.x) + parseFloat(d.target.x)) / 2;
            return x;
          })
          .attr("y", function(d) {
            if (!d.source.y || !d.target.y) return 0;
            var y = (parseFloat(d.source.y) + parseFloat(d.target.y)) / 2;
            return y;
          });
        // 更新节点坐标
        graphNode
          .attr("cx", function(d) {
            return d.x;
          })
          .attr("cy", function(d) {
            return d.y;
          });
        // 更新节点操作按钮组坐标
        graphNodeButtonGroup
          .attr("cx", function(d) {
            return d.x;
          })
          .attr("cy", function(d) {
            return d.y;
          })
          .attr("transform", function(d) {
            return "translate(" + d.x + "," + d.y + ") scale(1)";
          });
        // 更新文字坐标
        graphNodeText
          .attr("x", function(d) {
            return d.x;
          })
          .attr("y", function(d) {
            return d.y;
          });
      }
      _this.simulation.force("link").links(links);
      _this.simulation.force(
        "center",
        d3.forceCenter(_this.width / 2, _this.height / 2)
      );
      _this.simulation.alpha(1).restart();
      // 鼠标滚轮缩放
      _this.zoom = d3
        .zoom()
        .scaleExtent([0.1, 4])
        .on("zoom", _this.zoomed);
      _this.svg.call(_this.zoom);
      // 静止双击缩放
      _this.svg.on("dblclick.zoom", null);
      //为按钮组绑定事件
      _this.bindEventButtonGroup();
    },
    dragStarted(d) {
      if (!d3.event.active) this.simulation.alphaTarget(0.8).restart();
      d.fx = d.x;
      d.fy = d.y;
    },
    dragged(d) {
      d.fx = d3.event.x;
      d.fy = d3.event.y;
    },
    dragEnded(d) {
      if (!d3.event.active) this.simulation.alphaTarget(0);
      d.fx = d3.event.x;
      d.fy = d3.event.y;
    },
    zoomed() {
      d3.selectAll(".node").attr("transform", d3.event.transform);
      d3.selectAll(".nodetext text").attr("transform", d3.event.transform);
      d3.selectAll(".line").attr("transform", d3.event.transform);
      d3.selectAll(".linetext text").attr("transform", d3.event.transform);
      d3.selectAll(".nodebutton").attr("transform", d3.event.transform);
      //_this.svg.selectAll("g").attr("transform", d3.event.transform);
    },
    zoomClick(direction) {
      var self = this;
      var factor = 0.2;
      var targetZoom = 1;
      var extent = self.zoom.scaleExtent();
      targetZoom = 1 + factor * direction;
      if (targetZoom < extent[0] || targetZoom > extent[1]) {
        return false;
      }
      self.zoom.scaleBy(self.svg, targetZoom); // 执行该方法后 会触发zoom事件
    },
    zoomIn() {
      this.zoomClick(1);
    },
    zoomOut() {
      this.zoomClick(-1);
    },
    refresh() {
      this.svg.call(this.zoom.transform, d3.zoomIdentity);
    },
    showFull() {
      this.isFullscreen = !this.isFullscreen;
      var full = document.getElementById("kg_container");
      this.fullScreen(full);
    },
    fullScreen(element) {
      if (element.requestFullscreen) {
        element.requestFullscreen();
      } else if (element.mozRequestFullScreen) {
        element.mozRequestFullScreen();
      } else if (element.webkitRequestFullscreen) {
        element.webkitRequestFullscreen();
      } else if (element.msRequestFullscreen) {
        element.msRequestFullscreen();
      }
    },
    exitFullScreen() {
      this.isFullscreen = !this.isFullscreen;
      if (document.exitFullscreen) {
        document.exitFullscreen();
      } else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen();
      } else if (document.webkitExitFullscreen) {
        document.webkitExitFullscreen();
      }
    },
    btnCollapseNode() {},
    btnOpenNode() {},
    close() {}
  }
};
</script>
<style>
.svg-set-box {
  height: 46px;
  line-height: 46px;
  padding-left: 15px;
  color: #7f7f7f;
  /* background: #f7f7f7; */
  position: absolute;
  bottom: 0;
}
.ctwh-dibmr {
  display: inline-block;
}
.ss-d {
  display: inline-block;
  vertical-align: middle;
  margin-right: 10px;
  border-radius: 50%;
  background: #dedede;
}
.sd1 {
  width: 30px;
  height: 30px;
}
.sd2 {
  width: 26px;
  height: 26px;
}
.sd3 {
  width: 20px;
  height: 20px;
}
.sd4 {
  width: 16px;
  height: 16px;
}
.sd-active {
  background: #08aefc !important;
}
.toolbar {
  margin-left: 150px;
  margin-right: 15px;
  line-height: 18px;
}
ul,
li {
  list-style: none;
}
.toolbar li {
  float: left;
  width: 60px;
}
.notshow {
  display: none;
}
.nodetext {
  pointer-events: all;
  cursor: pointer;
  stroke-dasharray: 0 0 0 0;
  stroke-dashoffset: 10;
  transition: all ease 0.1s;
}
.nodetext:hover {
  stroke-dashoffset: 0;
  stroke-dasharray: 100;
}
</style>
