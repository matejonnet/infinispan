/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other
 * contributors as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.infinispan.cdi.test.cachemanager.external;

import org.infinispan.cdi.Infinispan;
import org.infinispan.config.Configuration;

import javax.enterprise.inject.Produces;

/**
 * Creates a number of caches, based on come external mechanism as de
 *
 * @author Pete Muir
 */
public class Config {

   /**
    * Associate the externally defined "large" cache with the qualifier {@link Large}
    */
   @Produces
   @Infinispan("large")
   @Large
   Configuration largeconfiguration;

   /**
    * Associate the externally defined "quick" cache with the qualifier {@link Quick}
    */
   @Produces
   @Infinispan("quick")
   @Quick
   Configuration configuration;
}
