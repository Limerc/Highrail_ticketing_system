import{q as b,T as h,_ as q,b as S}from"./TrainTicketInfo-BxrpNpg-.js";import{d as B,r as o,o as R,w as x,c as y,a as i,b as C,F as v,e as F,u as $,f as D,s as E,g as N,h as V,i as c,Q as I,j as Q,k,l as j,m as L,_ as M}from"./index-DeSWxAP-.js";const z={class:"train-list"},A=B({__name:"Trains",setup(G){const g=$(),r=D(),l=o(),d=o([]),t=o(r.query),_=o();async function f(){var a;t.value=r.query;const s=await b(t.value);d.value=s.map(E),(a=_.value)==null||a.assignData({...t.value,start_date:N(t.value.start_date)})}async function w(s,a,u,e,m,n){var p;const T=`${(n.hours+n.days*24).toString().padStart(2,"0")}:${n.minutes.toString().padStart(2,"0")}:${n.wholeSeconds.toString().padStart(2,"0")}`;await S({h_id:s,begin_station:a,arrive_station:u,begin_station_interval:e,arrive_station_interval:m,duration:T}),(p=l.value)==null||p.show("success","购票成功!")}return R(()=>{f()}),x(()=>r.query,()=>{f()}),(s,a)=>{const u=V("ElButton");return c(),y(v,null,[i(I,{ref_key:"queryFormRef",ref:_,inline:""},null,512),C("ul",z,[(c(!0),y(v,null,F(d.value,e=>(c(),Q(h,{data:{...e,...t.value}},{default:k(()=>[i(u,{type:"primary",onClick:m=>w(e.h_id,e.begin_station,e.arrive_station,e.begin_tid,e.arrive_tid,e.duration)},{default:k(()=>a[0]||(a[0]=[j("购票")])),_:2},1032,["onClick"])]),_:2},1032,["data"]))),256))]),i(q,{ref_key:"dialogRef",ref:l,"count-down-mode":"","count-down-callback":()=>L(g).push({name:"myTickets"})},null,8,["count-down-callback"])],64)}}}),K=M(A,[["__scopeId","data-v-6c53ded4"]]);export{K as default};