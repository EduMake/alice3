/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package org.lgna.project.ast;

import org.lgna.project.ast.localizer.AstLocalizer;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractNode extends Element implements Node {

	private static org.lgna.project.ast.localizer.AstLocalizerFactory astLocalizerFactory = new org.lgna.project.ast.localizer.DefaultAstLocalizerFactory();

	public static org.lgna.project.ast.localizer.AstLocalizerFactory getAstLocalizerFactory() {
		return AbstractNode.astLocalizerFactory;
	}

	public static void setAstLocalizerFactory( org.lgna.project.ast.localizer.AstLocalizerFactory astLocalizerFactory ) {
		AbstractNode.astLocalizerFactory = astLocalizerFactory;
	}

	private java.util.UUID id = java.util.UUID.randomUUID();
	private AbstractNode parent;

	@Override
	public final java.util.UUID getId() {
		return this.id;
	}

	public final void setId( java.util.UUID id ) {
		this.id = id;
	}

	@Override
	public boolean contentEquals( Node other, ContentEqualsStrictness strictness, edu.cmu.cs.dennisc.property.PropertyFilter filter ) {
		if( other != null ) {
			Class<?> thisCls = this.getClass();
			Class<?> otherCls = other.getClass();
			return thisCls.equals( otherCls );
		} else {
			return false;
		}
	}

	@Override
	public final boolean contentEquals( org.lgna.project.ast.Node other, org.lgna.project.ast.ContentEqualsStrictness strictness ) {
		edu.cmu.cs.dennisc.property.PropertyFilter filter = null;
		return this.contentEquals( other, strictness, filter );
	}

	@Override
	public Node getParent() {
		return this.parent;
	}

	private void setParent( AbstractNode parent ) {
		if( this.parent != parent ) {
			if( this.parent != null ) {
				if( parent != null ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "previous not null", this, this.parent );
				}
			}
			this.parent = parent;
		}
	}

	@Override
	public <N extends Node> N getFirstAncestorAssignableTo( Class<N> cls, boolean isThisIncludedInSearch ) {
		Node rv;
		if( isThisIncludedInSearch ) {
			rv = this;
		} else {
			rv = this.getParent();
		}
		while( rv != null ) {
			if( cls.isAssignableFrom( rv.getClass() ) ) {
				break;
			}
			rv = rv.getParent();
		}
		return (N)rv;
	}

	@Override
	public final <N extends Node> N getFirstAncestorAssignableTo( Class<N> cls ) {
		return getFirstAncestorAssignableTo( cls, false );
	}

	@Override
	public void firePropertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		super.firePropertyChanging( e );
		edu.cmu.cs.dennisc.property.InstanceProperty<?> property = e.getTypedSource();
		if( property instanceof NodeProperty<?> ) {
			NodeProperty<?> nodeProperty = (NodeProperty<?>)property;
			boolean isReference;
			if( nodeProperty instanceof DeclarationProperty<?> ) {
				isReference = ( (DeclarationProperty<?>)nodeProperty ).isReference();
			} else {
				isReference = false;
			}
			if( isReference ) {
				//pass
			} else {
				AbstractNode node = (AbstractNode)nodeProperty.getValue();
				if( node != null ) {
					node.setParent( null );
				}
			}
		}
	}

	@Override
	public void firePropertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		edu.cmu.cs.dennisc.property.InstanceProperty<?> property = e.getTypedSource();
		if( property instanceof NodeProperty<?> ) {
			NodeProperty<?> nodeProperty = (NodeProperty<?>)property;
			boolean isReference;
			if( nodeProperty instanceof DeclarationProperty<?> ) {
				isReference = ( (DeclarationProperty<?>)nodeProperty ).isReference();
			} else {
				isReference = false;
			}
			if( isReference ) {
				//pass
			} else {
				AbstractNode node = (AbstractNode)nodeProperty.getValue();
				if( node != null ) {
					node.setParent( this );
				}
			}
		}
		super.firePropertyChanged( e );
	}

	@Override
	public void fireClearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent e ) {
		super.fireClearing( e );
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty<?> ) {
			NodeListProperty<?> nodeListProperty = (NodeListProperty<?>)listProperty;
			for( Node node : nodeListProperty ) {
				if( node instanceof AbstractNode ) {
					( (AbstractNode)node ).setParent( null );
				}
			}
		}
	}

	@Override
	public void fireRemoving( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent e ) {
		super.fireRemoving( e );
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty<?> ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof AbstractNode ) {
					( (AbstractNode)o ).setParent( null );
				}
			}
		}
	}

	@Override
	public void fireSetting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e ) {
		super.fireSetting( e );
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty<?> ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof AbstractNode ) {
					( (AbstractNode)o ).setParent( null );
				}
			}
		}
	}

	@Override
	public void fireSet( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e ) {
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty<?> ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof AbstractNode ) {
					( (AbstractNode)o ).setParent( this );
				}
			}
		}
		super.fireSet( e );
	}

	@Override
	public void fireAdding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e ) {
		super.fireAdding( e );
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty<?> ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof AbstractNode ) {
					( (AbstractNode)o ).setParent( null );
				}
			}
		}
	}

	@Override
	public void fireAdded( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e ) {
		edu.cmu.cs.dennisc.property.ListProperty<?> listProperty = (edu.cmu.cs.dennisc.property.ListProperty<?>)e.getSource();
		if( listProperty instanceof NodeListProperty<?> ) {
			//NodeListProperty< ? > nodeListProperty = (NodeListProperty< ? >)listProperty;
			for( Object o : e.getElements() ) {
				if( o instanceof AbstractNode ) {
					( (AbstractNode)o ).setParent( this );
				}
			}
		}
		super.fireAdded( e );
	}

	private static void acceptIfCrawlable( edu.cmu.cs.dennisc.pattern.Crawler crawler, java.util.Set<edu.cmu.cs.dennisc.pattern.Crawlable> visited, Object value, CrawlPolicy crawlPolicy, edu.cmu.cs.dennisc.pattern.Criterion<Declaration> declarationFilter ) {
		if( value instanceof AbstractNode ) {
			AbstractNode node = (AbstractNode)value;
			if( declarationFilter != null ) {
				if( node instanceof Declaration ) {
					Declaration declaration = (Declaration)node;
					if( declarationFilter.accept( declaration ) ) {
						//pass
					} else {
						edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "skipping", declaration );
						return;
					}
				}
			}
			node.accept( crawler, visited, crawlPolicy, declarationFilter );
		} else if( value instanceof edu.cmu.cs.dennisc.pattern.Crawlable ) {
			edu.cmu.cs.dennisc.pattern.Crawlable crawlable = (edu.cmu.cs.dennisc.pattern.Crawlable)value;
			crawlable.accept( crawler, visited );
		}
	}

	@Override
	public void accept( edu.cmu.cs.dennisc.pattern.Crawler crawler, java.util.Set<edu.cmu.cs.dennisc.pattern.Crawlable> visited ) {
		this.accept( crawler, visited, CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY, null );
	}

	private void accept( edu.cmu.cs.dennisc.pattern.Crawler crawler, java.util.Set<edu.cmu.cs.dennisc.pattern.Crawlable> visited, CrawlPolicy crawlPolicy, edu.cmu.cs.dennisc.pattern.Criterion<Declaration> declarationFilter ) {
		if( visited.contains( this ) ) {
			//pass
		} else {
			visited.add( this );
			crawler.visit( this );

			// Look through this nodes properties to see if any have anything to crawl
			for( edu.cmu.cs.dennisc.property.InstanceProperty<?> property : this.getProperties() ) {
				// Check if this is a reference
				if( property instanceof DeclarationProperty<?> ) {
					DeclarationProperty<?> declarationProperty = (DeclarationProperty<?>)property;
					if( declarationProperty.isReference() ) {
						if( crawlPolicy.isReferenceTunneledInto() ) {
							//pass
						} else {
							if( crawlPolicy.isReferenceIncluded() ) {
								Declaration declaration = declarationProperty.getValue();
								if( visited.contains( declaration ) ) {
									//pass
								} else {
									visited.add( declaration );
									crawler.visit( declaration );
								}
							}
							continue;
						}
					}
				}

				Object value = property.getValue();
				if( value instanceof Iterable<?> ) {
					Iterable<?> iterable = (Iterable<?>)value;
					for( Object item : iterable ) {
						acceptIfCrawlable( crawler, visited, item, crawlPolicy, declarationFilter );
					}
				} else if( value instanceof Object[] ) {
					Object[] array = (Object[])value;
					for( Object item : array ) {
						acceptIfCrawlable( crawler, visited, item, crawlPolicy, declarationFilter );
					}
				} else {
					acceptIfCrawlable( crawler, visited, value, crawlPolicy, declarationFilter );
				}
			}
		}
	}

	@Override
	public final synchronized void crawl( edu.cmu.cs.dennisc.pattern.Crawler crawler, CrawlPolicy crawlPolicy, edu.cmu.cs.dennisc.pattern.Criterion<Declaration> criterion ) {
		this.accept( crawler, new java.util.HashSet<edu.cmu.cs.dennisc.pattern.Crawlable>(), crawlPolicy, criterion );
	}

	public final synchronized void crawl( edu.cmu.cs.dennisc.pattern.Crawler crawler, CrawlPolicy crawlPolicy ) {
		this.crawl( crawler, crawlPolicy, null );
	}

	// hashCode not terrible choice for "unique" key.
	//	private static int getNotGuaranteedToBeUniqueKey( AbstractDeclaration declaration ) {
	//		return System.identityHashCode( declaration );
	//	}

	protected java.util.Set<AbstractDeclaration> fillInDeclarationSet( java.util.Set<AbstractDeclaration> rv, java.util.Set<AbstractNode> nodes ) {
		nodes.add( this );
		for( edu.cmu.cs.dennisc.property.InstanceProperty<?> property : this.getProperties() ) {
			Object value = property.getValue();
			if( value instanceof AbstractNode ) {
				if( nodes.contains( value ) ) {
					//pass
				} else {
					( (AbstractNode)value ).fillInDeclarationSet( rv, nodes );
				}
			} else if( value instanceof Iterable<?> ) {
				for( Object item : (Iterable<?>)value ) {
					if( item instanceof AbstractNode ) {
						if( nodes.contains( item ) ) {
							//pass
						} else {
							( (AbstractNode)item ).fillInDeclarationSet( rv, nodes );
						}
					}
				}
			}
		}
		return rv;
	}

	public java.util.Set<AbstractDeclaration> createDeclarationSet() {
		java.util.Set<AbstractDeclaration> rv = new java.util.HashSet<AbstractDeclaration>();
		fillInDeclarationSet( rv, new java.util.HashSet<AbstractNode>() );
		return rv;
	}

	private java.util.Set<AbstractDeclaration> removeDeclarationsThatNeedToBeCopied( java.util.Set<AbstractDeclaration> rv, java.util.Set<AbstractNode> nodes ) {
		nodes.add( this );
		for( edu.cmu.cs.dennisc.property.InstanceProperty<?> property : this.getProperties() ) {
			if( property instanceof DeclarationProperty ) {
				DeclarationProperty<? extends AbstractDeclaration> declarationProperty = (DeclarationProperty<? extends AbstractDeclaration>)property;
				if( declarationProperty.isReference() ) {
					//pass
				} else {
					rv.remove( declarationProperty.getValue() );
				}
			}
			Object value = property.getValue();
			if( value instanceof AbstractNode ) {
				if( nodes.contains( value ) ) {
					//pass
				} else {
					( (AbstractNode)value ).removeDeclarationsThatNeedToBeCopied( rv, nodes );
				}
			} else if( value instanceof Iterable<?> ) {
				for( Object item : (Iterable<?>)value ) {
					if( item instanceof AbstractNode ) {
						if( nodes.contains( item ) ) {
							//pass
						} else {
							( (AbstractNode)item ).removeDeclarationsThatNeedToBeCopied( rv, nodes );
						}
					}
				}
			}
		}
		return rv;
	}

	public java.util.Set<AbstractDeclaration> removeDeclarationsThatNeedToBeCopied( java.util.Set<AbstractDeclaration> rv ) {
		return removeDeclarationsThatNeedToBeCopied( rv, new java.util.HashSet<AbstractNode>() );
	}

	//	protected StringBuilder appendRepr( StringBuilder rv, java.util.Locale locale ) {
	//		rv.append( this.getClass().getSimpleName() );
	//		return rv;
	//	}
	//
	//	public final String getRepr( java.util.Locale locale ) {
	//		StringBuilder sb = new StringBuilder();
	//		this.appendRepr( sb, locale );
	//		return sb.toString();
	//	}

	public static void safeAppendRepr( AstLocalizer localizer, Node node ) {
		if( node instanceof AbstractNode ) {
			( (AbstractNode)node ).appendRepr( localizer );
		} else {
			if( node != null ) {
				localizer.appendText( node.getRepr() );
			} else {
				localizer.appendNull();
			}
		}
	}

	//protected abstract void appendRepr( AstLocalizer localizer );
	protected void appendRepr( AstLocalizer localizer ) {
		localizer.appendText( this.getClass().getSimpleName() );
	}

	@Override
	public final String getRepr() {
		final StringBuilder sb = new StringBuilder();
		this.appendRepr( astLocalizerFactory.createInstance( sb ) );
		return sb.toString();
	}

	@Override
	public String generateLocalName( UserLocal local ) {
		// This is overridden in the loop subclasses that will display the name.
		return "unusedName";
	}

	protected StringBuilder appendStringDetails( StringBuilder rv ) {
		return rv;
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getSimpleName() );
		sb.append( "[" );
		this.appendStringDetails( sb );
		sb.append( "]" );
		return sb.toString();
	}
}
