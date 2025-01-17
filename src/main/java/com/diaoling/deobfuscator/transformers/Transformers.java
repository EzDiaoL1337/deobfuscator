/*
 * Copyright 2016 Sam Sun <me@samczsun.com>
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.diaoling.deobfuscator.transformers;

import com.diaoling.deobfuscator.transformers.general.peephole.PeepholeOptimizer;
import com.diaoling.deobfuscator.transformers.general.removers.IllegalSignatureRemover;
import com.diaoling.deobfuscator.transformers.general.removers.IllegalVarargsRemover;
import com.diaoling.deobfuscator.transformers.general.removers.LineNumberRemover;
import com.diaoling.deobfuscator.transformers.general.removers.SyntheticBridgeRemover;
import com.diaoling.deobfuscator.transformers.normalizer.*;
import com.diaoling.deobfuscator.transformers.skidsuite2.FakeExceptionTransformer;
import com.diaoling.deobfuscator.transformers.skidsuite2.StringEncryptionTransformer;
import com.diaoling.deobfuscator.transformers.smoke.IllegalVariableTransformer;
import com.diaoling.deobfuscator.transformers.smoke.NumberObfuscationTransformer;
import com.diaoling.deobfuscator.transformers.stringer.HideAccessObfuscationTransformer;
import com.diaoling.deobfuscator.transformers.stringer.InvokedynamicTransformer;
import com.diaoling.deobfuscator.transformers.stringer.ReflectionObfuscationTransformer;
import com.diaoling.deobfuscator.transformers.stringer.ResourceEncryptionTransformer;
import com.diaoling.deobfuscator.transformers.zelix.FlowObfuscationTransformer;
import com.diaoling.deobfuscator.transformers.zelix.string.EnhancedStringEncryptionTransformer;
import com.diaoling.deobfuscator.transformers.zelix.string.SimpleStringEncryptionTransformer;

public class Transformers {
    public static class Stringer {
        public static final Class<? extends Transformer> STRING_ENCRYPTION = com.diaoling.deobfuscator.transformers.stringer.StringEncryptionTransformer.class;
        public static final Class<? extends Transformer> INVOKEDYNAMIC = InvokedynamicTransformer.class;
        public static final Class<? extends Transformer> REFLECTION_OBFUSCATION = ReflectionObfuscationTransformer.class;
        public static final Class<? extends Transformer> HIDEACCESS_OBFUSCATION = HideAccessObfuscationTransformer.class;
        public static final Class<? extends Transformer> RESOURCE_ENCRYPTION = ResourceEncryptionTransformer.class;
    }
    
    public static class General {
        public static final Class<? extends Transformer> PEEPHOLE_OPTIMIZER = PeepholeOptimizer.class;

        public static class Removers {
            public static final Class<? extends Transformer> ILLEGAL_VARARGS = IllegalVarargsRemover.class;
            public static final Class<? extends Transformer> LINE_NUMBERS = LineNumberRemover.class;
            public static final Class<? extends Transformer> ILLEGAL_SIGNATURE = IllegalSignatureRemover.class;
            public static final Class<? extends Transformer> SYNTHETIC_BRIDGE = SyntheticBridgeRemover.class;
        }
    }
    
    public static class Smoke {
    	public static final Class<? extends Transformer> STRING_ENCRYPTION = com.diaoling.deobfuscator.transformers.smoke.StringEncryptionTransformer.class;
        public static final Class<? extends Transformer> NUMBER_OBFUSCATION = NumberObfuscationTransformer.class;
        public static final Class<? extends Transformer> ILLEGAL_VARIABLE = IllegalVariableTransformer.class;
    }
    
    public static class Zelix {
        public static final Class<? extends Transformer> STRING_ENCRYPTION_SIMPLE = SimpleStringEncryptionTransformer.class;
        public static final Class<? extends Transformer> STRING_ENCRYPTION_ENHANCED = EnhancedStringEncryptionTransformer.class;
        public static final Class<? extends Transformer> REFLECTION_OBFUSCATION = com.diaoling.deobfuscator.transformers.zelix.ReflectionObfuscationTransformer.class;
        public static final Class<? extends Transformer> FLOW_OBFUSCATION = FlowObfuscationTransformer.class;
    }

    public static class SkidSuite {
        public static final Class<? extends Transformer> STRING_ENCRYPTION = StringEncryptionTransformer.class;
        public static final Class<? extends Transformer> FAKE_EXCEPTION = FakeExceptionTransformer.class;
    }

    public static class Normalizer {
        public static final Class<? extends Transformer> PACKAGE_NORMALIZER = PackageNormalizer.class;
        public static final Class<? extends Transformer> CLASS_NORMALIZER = ClassNormalizer.class;
        public static final Class<? extends Transformer> FIELD_NORMALIZER = FieldNormalizer.class;
        public static final Class<? extends Transformer> METHOD_NORMALIZER = MethodNormalizer.class;
        public static final Class<? extends Transformer> SOURCEFILE_CLASS_NORMALIZER = SourceFileClassNormalizer.class;
        public static final Class<? extends Transformer> DUPLICATE_RENAMER = DuplicateRenamer.class;
    }
}
