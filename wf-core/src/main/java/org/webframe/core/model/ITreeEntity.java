/*
 * wf-core
 * Created on 2012-2-2-上午09:33:39
 */

package org.webframe.core.model;

/**
 * 树形实体类接口
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-2-2 上午09:33:39
 * @version
 */
public interface ITreeEntity {

	/**
	 * 编码，业务主键
	 * 
	 * @return
	 * @author 黄国庆 2012-2-2 上午09:38:53
	 */
	public String getCode();

	/**
	 * 节点所在层级
	 * 
	 * @return
	 * @author 黄国庆 2012-2-2 上午09:39:01
	 */
	public int getLevel();

	/**
	 * 节点描述
	 * 
	 * @return
	 * @author 黄国庆 2012-2-2 上午09:39:28
	 */
	public String getDescription();

	/**
	 * 节点的父节点非业务主键
	 * 
	 * @return
	 * @author 黄国庆 2012-2-2 上午09:39:56
	 */
	public String getParentId();

	/**
	 * 当前节点子节点的个数；如果{@link #isLeaf()}返回true，则返回值为0
	 * 
	 * @return 大于等于零的整数
	 * @author 黄国庆 2012-2-2 上午09:40:36
	 */
	public int getChildCount();

	/**
	 * 当前节点是否是子节点
	 * 
	 * @return
	 * @author 黄国庆 2012-2-2 上午09:41:15
	 */
	public boolean isLeaf();
}
