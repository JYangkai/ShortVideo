precision mediump float;
uniform sampler2D uSampler;
varying vec2 vCoordinate;
// 模糊半径
uniform int uBlurRadius;
// 模糊步长
uniform vec2 uBlurOffset;
// PI
const float PI = 3.1415926;

// 边界值处理
vec2 clampCoordinate(vec2 coordinate){
    return vec2(clamp(coordinate.x, 0.0, 1.0), clamp(coordinate.y, 0.0, 1.0));
}

// 获取权重
float getWeight(int i, vec3 curColor, vec3 centerColor){
    float colorDistance = distance(curColor, centerColor);
    float sigma = float(uBlurRadius) / 3.0;
    return (1.0 / sqrt(2.0 * PI * sigma * sigma)) * exp(-float(i * i) / (2.0 * sigma * sigma)) * (1.0 - colorDistance);
}

void main(){
    vec4 sourceColor = texture2D(uSampler, vCoordinate);

    if (uBlurRadius <= 1){
        gl_FragColor = sourceColor;
        return;
    }

    float sumWeight = getWeight(0, sourceColor.rgb, sourceColor.rgb);

    vec3 finalColor = sourceColor.rgb * sumWeight;

    for (int i = 1; i < uBlurRadius; i++) {
        vec2 curCoordinate1 = clampCoordinate(vCoordinate - uBlurOffset * float(i));
        vec2 curCoordinate2 = clampCoordinate(vCoordinate + uBlurOffset * float(i));

        vec3 curColor1 = texture2D(uSampler, curCoordinate1).rgb;
        vec3 curColor2 = texture2D(uSampler, curCoordinate2).rgb;

        float weight1 = getWeight(i, curColor1, sourceColor.rgb);
        float weight2 = getWeight(i, curColor2, sourceColor.rgb);

        sumWeight += weight1;
        sumWeight += weight2;

        finalColor += curColor1 * weight1;
        finalColor += curColor2 * weight1;
    }

    finalColor /= sumWeight;

    gl_FragColor = vec4(finalColor, 1.0);
}