$wnd.com_qrsynergy_MyAppWidgetset.runAsyncCallback7("function gAc(){}\nfunction iAc(){}\nfunction CRd(){FNd.call(this)}\nfunction ztb(a,b){this.a=b;this.b=a}\nfunction Xsb(a,b){Grb(a,b);--a.b}\nfunction Zl(a){return (hk(),a).createElement('col')}\nfunction w7c(){TTb.call(this);this.a=$z(vRb(hcb,this),2628)}\nfunction P7c(){opb.call(this);this.d=1;this.a=1;this.c=false;lpb(this,M7c(this),0,0)}\nfunction O7c(a,b,c){a.d=b;a.a=c;mpb(a,a.b);lpb(a,M7c(a),0,0)}\nfunction bqc(a,b,c){wRb(a.a,new mAc(new EAc(hcb),xhe),oz(gz(_fb,1),Nfe,1,5,[T$d(b),T$d(c)]))}\nfunction M7c(a){a.b=new $sb(a.d,a.a);_nb(a.b,dze);Tnb(a.b,dze);tob(a.b,a,(gt(),gt(),ft));return a.b}\nfunction zrb(a,b){var c,d,e;e=Crb(a,b.c);if(!e){return null}d=nk((hk(),e)).sectionRowIndex;c=e.cellIndex;return new ztb(d,c)}\nfunction $sb(a,b){Mrb.call(this);Hrb(this,new csb(this));Krb(this,new Htb(this));Irb(this,new Ctb(this));Ysb(this,b);Zsb(this,a)}\nfunction Wsb(a,b){if(b<0){throw Mib(new eZd('Cannot access a row with a negative index: '+b))}if(b>=a.b){throw Mib(new eZd(Oke+b+Pke+a.b))}}\nfunction Zsb(a,b){if(a.b==b){return}if(b<0){throw Mib(new eZd('Cannot set number of rows to '+b))}if(a.b<b){_sb((glb(),a.M),b-a.b,a.a);a.b=b}else{while(a.b>b){Xsb(a,a.b-1)}}}\nfunction Btb(a,b,c){var d,e;b=$wnd.Math.max(b,1);e=a.a.childNodes.length;if(e<b){for(d=e;d<b;d++){hj(a.a,Zl($doc))}}else if(!c&&e>b){for(d=e;d>b;d--){qj(a.a,a.a.lastChild)}}}\nfunction Crb(a,b){var c,d,e;d=(glb(),(hk(),gk).qc(b));for(;d;d=(null,nk(d))){if(x_d(Hj(d,'tagName'),'td')){e=(null,nk(d));c=(null,nk(e));if(c==a.M){return d}}if(d==a.M){return null}}return null}\nfunction N7c(a,b,c,d){var e,f;if(b!=null&&c!=null&&d!=null){if(b.length==c.length&&c.length==d.length){for(e=0;e<b.length;e++){f=$rb(a.b.N,uZd(c[e],10),uZd(d[e],10));f.style[hEe]=b[e]}}a.c=true}}\nfunction _sb(a,b,c){var d=$doc.createElement('td');d.innerHTML=Tme;var e=$doc.createElement(zhe);for(var f=0;f<c;f++){var g=d.cloneNode(true);e.appendChild(g)}a.appendChild(e);for(var h=1;h<b;h++){a.appendChild(e.cloneNode(true))}}\nfunction Ysb(a,b){var c,d,e,f,g,h,j;if(a.a==b){return}if(b<0){throw Mib(new eZd('Cannot set number of columns to '+b))}if(a.a>b){for(c=0;c<a.b;c++){for(d=a.a-1;d>=b;d--){vrb(a,c,d);e=xrb(a,c,d,false);f=Etb(a.M,c);f.removeChild(e)}}}else{for(c=0;c<a.b;c++){for(d=a.a;d<b;d++){g=Etb(a.M,c);h=(j=(glb(),tm($doc)),j.innerHTML=Tme,glb(),j);elb.Pd(g,ulb(h),d)}}}a.a=b;Btb(a.O,b,false)}\nfunction cAc(c){var d={setter:function(a,b){a.a=b},getter:function(a){return a.a}};c.lk(icb,zEe,d);var d={setter:function(a,b){a.b=b},getter:function(a){return a.b}};c.lk(icb,AEe,d);var d={setter:function(a,b){a.c=b},getter:function(a){return a.c}};c.lk(icb,BEe,d);var d={setter:function(a,b){a.d=b.ep()},getter:function(a){return T$d(a.d)}};c.lk(icb,CEe,d);var d={setter:function(a,b){a.e=b.ep()},getter:function(a){return T$d(a.e)}};c.lk(icb,DEe,d)}\nvar zEe='changedColor',AEe='changedX',BEe='changedY',CEe='columnCount',DEe='rowCount';njb(830,799,Qke,$sb);_.Ie=function atb(a){return this.a};_.Je=function btb(){return this.b};_.Ke=function ctb(a,b){Wsb(this,a);if(b<0){throw Mib(new eZd('Cannot access a column with a negative index: '+b))}if(b>=this.a){throw Mib(new eZd(Mke+b+Nke+this.a))}};_.Le=function dtb(a){Wsb(this,a)};_.a=0;_.b=0;var UG=$Zd(Ake,'Grid',830,$G);njb(2176,1,{},ztb);_.a=0;_.b=0;var XG=$Zd(Ake,'HTMLTable/Cell',2176,_fb);njb(1928,1,Rle);_.$b=function fAc(){XAc(this.b,icb,Sab);MAc(this.b,kre,k3);NAc(this.b,k3,new gAc);NAc(this.b,icb,new iAc);VAc(this.b,k3,yme,new EAc(icb));cAc(this.b);TAc(this.b,icb,zEe,new EAc(gz(fgb,1)));TAc(this.b,icb,AEe,new EAc(gz(fgb,1)));TAc(this.b,icb,BEe,new EAc(gz(fgb,1)));TAc(this.b,icb,CEe,new EAc(Ufb));TAc(this.b,icb,DEe,new EAc(Ufb));KAc(this.b,k3,new sAc(l$,mre,oz(gz(fgb,1),Ofe,2,6,[Zme,nre])));KAc(this.b,k3,new sAc(l$,ore,oz(gz(fgb,1),Ofe,2,6,[pre])));Bcc((!tcc&&(tcc=new Jcc),tcc),this.a.d)};njb(1930,1,dxe,gAc);_.dk=function hAc(a,b){return new w7c};var EZ=$Zd(rpe,'ConnectorBundleLoaderImpl/7/1/1',1930,_fb);njb(1931,1,dxe,iAc);_.dk=function jAc(a,b){return new CRd};var FZ=$Zd(rpe,'ConnectorBundleLoaderImpl/7/1/2',1931,_fb);njb(1929,31,iEe,w7c);_.eg=function y7c(){return !this.P&&(this.P=mFb(this)),$z($z(this.P,6),360)};_.fg=function z7c(){return !this.P&&(this.P=mFb(this)),$z($z(this.P,6),360)};_.vg=function A7c(){return !this.G&&(this.G=new P7c),$z(this.G,223)};_.Hh=function x7c(){return new P7c};_.Og=function B7c(){tob((!this.G&&(this.G=new P7c),$z(this.G,223)),this,(gt(),gt(),ft))};_.Oc=function C7c(a){bqc(this.a,(!this.G&&(this.G=new P7c),$z(this.G,223)).e,(!this.G&&(this.G=new P7c),$z(this.G,223)).f)};_.Dg=function D7c(a){LTb(this,a);(a.uh(DEe)||a.uh(CEe)||a.uh('updateGrid'))&&O7c((!this.G&&(this.G=new P7c),$z(this.G,223)),(!this.P&&(this.P=mFb(this)),$z($z(this.P,6),360)).e,(!this.P&&(this.P=mFb(this)),$z($z(this.P,6),360)).d);if(a.uh(AEe)||a.uh(BEe)||a.uh(zEe)||a.uh('updateColor')){N7c((!this.G&&(this.G=new P7c),$z(this.G,223)),(!this.P&&(this.P=mFb(this)),$z($z(this.P,6),360)).a,(!this.P&&(this.P=mFb(this)),$z($z(this.P,6),360)).b,(!this.P&&(this.P=mFb(this)),$z($z(this.P,6),360)).c);(!this.G&&(this.G=new P7c),$z(this.G,223)).c||wRb(this.a.a,new mAc(new EAc(hcb),'refresh'),oz(gz(_fb,1),Nfe,1,5,[]))}};var k3=$Zd(jEe,'ColorPickerGridConnector',1929,l$);njb(223,500,{50:1,58:1,21:1,8:1,19:1,20:1,18:1,36:1,40:1,22:1,39:1,16:1,14:1,223:1,26:1},P7c);_.Tc=function Q7c(a){return tob(this,a,(gt(),gt(),ft))};_.Oc=function R7c(a){var b;b=zrb(this.b,a);if(!b){return}this.f=b.b;this.e=b.a};_.a=0;_.c=false;_.d=0;_.e=0;_.f=0;var m3=$Zd(jEe,'VColorPickerGrid',223,tG);njb(360,13,{6:1,13:1,30:1,103:1,360:1,3:1},CRd);_.d=0;_.e=0;var icb=$Zd(nxe,'ColorPickerGridState',360,Sab);Afe(Dh)(7);\n//# sourceURL=com.qrsynergy.MyAppWidgetset-7.js\n")
