precision mediump float;
uniform sampler2D uYSampler;
uniform sampler2D uUSampler;
uniform sampler2D uVSampler;
varying vec2 vCoordinate;
void main(){
    float y = texture2D(uYSampler, vCoordinate).r;
    float u = texture2D(uUSampler, vCoordinate).r - 0.5;
    float v = texture2D(uVSampler, vCoordinate).r - 0.5;
    vec3 rgb = vec3(0.0);
    rgb.r = y + 1.403 * v;
    rgb.g = y - 0.344 * u - 0.714 * v;
    rgb.b = y + 1.770 * u;
    gl_FragColor = vec4(rgb, 1.0);
}