/**
 * Copyright (c) 2009/09-2012/08, Regents of the University of Colorado
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * Copyright 2012/09-2013/04, University of Massachusetts Amherst
 * Copyright 2013/05-Present, IPSoft Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.clearnlp.util;

import java.util.Set;

import org.apache.log4j.Logger;

import com.clearnlp.dependency.DEPTree;
import com.clearnlp.morphology.MPLib;
import com.clearnlp.reader.JointReader;
import com.clearnlp.util.map.Prob1DMap;
import com.google.common.collect.Sets;

/**
 * @since 2.0.2
 * @author Jinho D. Choi ({@code jdchoi77@gmail.com})
 */
public class UTCollect
{
	static public Set<String> getLowerSimplifiedFormsByDocumentFrequencies(Logger log, JointReader reader, String[] filenames, int cutoff, int maxCount)
	{
		Set<String> set = Sets.newHashSet();
		Prob1DMap map = new Prob1DMap();
		int j, len, count = 0;
		DEPTree tree;
		
		log.info(String.format("Collecting forms: cutoff = %d, max = %d\n", cutoff, maxCount));
		
		for (String filename : filenames)
		{
			reader.open(UTInput.createBufferedFileReader(filename));
			
			while ((tree = reader.next()) != null)
			{
				len = tree.size();
				
				for (j=1; j<len; j++)
					set.add(MPLib.getSimplifiedLowercaseWordForm(tree.get(j).form));
				
				if ((count += len) >= maxCount)
				{
					map.addAll(set);
					log.info(".");
					set.clear();
					count = 0;
				}
			}
			
			reader.close();
		}	log.info("\n");
		
		if (!set.isEmpty()) map.addAll(set);
		return map.toSet(cutoff);
	}
	
	static public Set<String> getLowerSimplifiedFormsByDocumentFrequencies(Logger log, JointReader reader, String[] filenames, int cutoff)
	{
		Set<String> set = Sets.newHashSet();
		Prob1DMap map = new Prob1DMap();
		DEPTree tree;
		int j, len;
		
		log.info(String.format("Collecting forms: cutoff = %d\n", cutoff));
		
		for (String filename : filenames)
		{
			reader.open(UTInput.createBufferedFileReader(filename));
			set.clear();
					
			while ((tree = reader.next()) != null)
			{
				len = tree.size();
				
				for (j=1; j<len; j++)
					set.add(MPLib.getSimplifiedLowercaseWordForm(tree.get(j).form));
			}
			
			map.addAll(set);
			reader.close();
			log.info(".");
		}	log.info("\n");
		
		return map.toSet(cutoff);
	}
}
