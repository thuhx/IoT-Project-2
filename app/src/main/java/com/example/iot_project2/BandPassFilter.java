package com.example.iot_project2;


/********************
 * matlab 中的bandpass和filter函数, 参考 http://www.doc88.com/p-1817605308696.html
 ********************/
public class BandPassFilter {
    private int center_freq;
    private int offset_freq;
    private int sample_freq;
    // 滤波器阶数
    private int M;
    private double[] h;

    public BandPassFilter(int center_freq_, int offset_freq_, int sample_freq_) {
        this.center_freq = center_freq_;
        this.offset_freq = offset_freq_;
        this.sample_freq = sample_freq_;
        double Wp1 = 2 * Math.PI * (center_freq - offset_freq) / sample_freq;
        double Wp2 = 2 * Math.PI * (center_freq + offset_freq) / sample_freq;

        int N = (int)Math.ceil(3.6 * sample_freq / offset_freq);
        M = N - 1;
        M += (M % 2);

        h = new double[M + 1];
        for (int k = 0; k <= M; k++) {
            if (k - M / 2 == 0) {
                h[k] = (Wp2 - Wp1) / Math.PI;
            }
            else {
                h[k] = Wp2 * Math.sin(Wp2 * (k - M / 2)) / (Math.PI * (Wp2 * (k - M / 2))) -
                        Wp1 * Math.sin(Wp1 * (k - M / 2)) / (Math.PI * (Wp1 * (k - M / 2)));
            }
        }
    }

    // 简单的matlab filter功能
    public double[] filter(double[] input) {
        int len = input.length;
        double[] output = new double[len];
        for (int i = 0; i < len; i++) {

            double y_front = 0;
            for(int j = 0; j <= M && j <= i; j++) {
                y_front += h[j] * input[i - j];
            }

            output[i] = y_front;
        }
        return output;
    }
}
