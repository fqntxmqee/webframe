/*
 * Ext图片组件的扩充
 * author: tiebiao
 * */
Ext.ns('Ext.ux.form');
Ext.ux.form.ImgField = Ext.extend(Ext.BoxComponent,{
	
	
	onRender:function(ct,position){
      if(!this.el){
            this.el = document.createElement('img');
            this.el.src = this.src;
            if(this.id){
             this.el.setAttribute('id',this.id);
            }
            if(this.callBack){
            this.el.onclick = this.callBack;
            }
            if(this.qtip){
           	 this.el["ext:qtip"] = this.qtip;
            }
      }
     
      
      
      Ext.form.Label.superclass.onRender.call(this,ct,position);
   },
    setSrc:function(src){
    	
       if(this.el)
       { 
         this.el.src = src ;
       }
      
      }
      
}); 
  Ext.reg('imgfield',Ext.ux.form.ImgField)