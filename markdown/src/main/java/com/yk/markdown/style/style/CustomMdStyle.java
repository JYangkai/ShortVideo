package com.yk.markdown.style.style;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.yk.markdown.style.bean.MdStyleBold;
import com.yk.markdown.style.bean.MdStyleBoldItalics;
import com.yk.markdown.style.bean.MdStyleCode;
import com.yk.markdown.style.bean.MdStyleCodeBlock;
import com.yk.markdown.style.bean.MdStyleItalics;
import com.yk.markdown.style.bean.MdStyleNormal;
import com.yk.markdown.style.bean.MdStyleOrderedList;
import com.yk.markdown.style.bean.MdStyleQuote;
import com.yk.markdown.style.bean.MdStyleSeparator;
import com.yk.markdown.style.bean.MdStyleTitle;
import com.yk.markdown.style.bean.MdStyleUnorderedList;

public class CustomMdStyle extends BaseMdStyle {
    private final SharedPreferences sp;

    public CustomMdStyle(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void init() {
        // 基础
        setBase(null);

        // 普通
        setNormal(
                new MdStyleNormal(
                        Color.parseColor(getValue(Sp.Normal.Key.TEXT_COLOR, Sp.Normal.Default.TEXT_COLOR)),
                        Integer.parseInt(getValue(Sp.Normal.Key.TEXT_SIZE, Sp.Normal.Default.TEXT_SIZE))
                )
        );

        // 引用
        setQuote(
                new MdStyleQuote(
                        Color.parseColor(getValue(Sp.Quote.Key.STRIPE_COLOR, Sp.Quote.Default.STRIPE_COLOR)),
                        Integer.parseInt(getValue(Sp.Quote.Key.STRIPE_WIDTH, Sp.Quote.Default.STRIPE_WIDTH)),
                        Integer.parseInt(getValue(Sp.Quote.Key.GAP_WIDTH, Sp.Quote.Default.GAP_WIDTH)),
                        Color.parseColor(getValue(Sp.Quote.Key.TEXT_COLOR, Sp.Quote.Default.TEXT_COLOR)),
                        Integer.parseInt(getValue(Sp.Quote.Key.TEXT_SIZE, Sp.Quote.Default.TEXT_SIZE))
                )
        );

        // 代码块
        setCodeBlock(
                new MdStyleCodeBlock(
                        Integer.parseInt(getValue(Sp.CodeBlock.Key.GAP_WIDTH, Sp.CodeBlock.Default.GAP_WIDTH)),
                        Color.parseColor(getValue(Sp.CodeBlock.Key.BACKGROUND_COLOR, Sp.CodeBlock.Default.BACKGROUND_COLOR)),
                        Color.parseColor(getValue(Sp.CodeBlock.Key.TEXT_COLOR, Sp.CodeBlock.Default.TEXT_COLOR)),
                        Integer.parseInt(getValue(Sp.CodeBlock.Key.TEXT_SIZE, Sp.CodeBlock.Default.TEXT_SIZE))
                )
        );

        // 有序列表
        setOrderedList(
                new MdStyleOrderedList(
                        Color.parseColor(getValue(Sp.OrderedList.Key.INDEX_COLOR, Sp.OrderedList.Default.INDEX_COLOR)),
                        Integer.parseInt(getValue(Sp.OrderedList.Key.INDEX_SIZE, Sp.OrderedList.Default.INDEX_SIZE)),
                        Integer.parseInt(getValue(Sp.OrderedList.Key.INDEX_WIDTH, Sp.OrderedList.Default.INDEX_WIDTH)),
                        Integer.parseInt(getValue(Sp.OrderedList.Key.GAP_WIDTH, Sp.OrderedList.Default.GAP_WIDTH)),
                        Color.parseColor(getValue(Sp.OrderedList.Key.TEXT_COLOR, Sp.OrderedList.Default.TEXT_COLOR)),
                        Integer.parseInt(getValue(Sp.OrderedList.Key.TEXT_SIZE, Sp.OrderedList.Default.TEXT_SIZE))
                )
        );

        // 无序列表
        setUnorderedList(
                new MdStyleUnorderedList(
                        Color.parseColor(getValue(Sp.UnorderedList.Key.CIRCLE_COLOR, Sp.UnorderedList.Default.CIRCLE_COLOR)),
                        Integer.parseInt(getValue(Sp.UnorderedList.Key.CIRCLE_RADIUS, Sp.UnorderedList.Default.CIRCLE_RADIUS)),
                        Integer.parseInt(getValue(Sp.UnorderedList.Key.GAP_WIDTH, Sp.UnorderedList.Default.GAP_WIDTH)),
                        Color.parseColor(getValue(Sp.UnorderedList.Key.TEXT_COLOR, Sp.UnorderedList.Default.TEXT_COLOR)),
                        Integer.parseInt(getValue(Sp.UnorderedList.Key.TEXT_SIZE, Sp.UnorderedList.Default.TEXT_SIZE))
                )
        );

        // 标题
        setTitle(
                new MdStyleTitle(
                        Color.parseColor(getValue(Sp.Title.Key.TEXT_COLOR, Sp.Title.Default.TEXT_COLOR)),
                        Integer.parseInt(getValue(Sp.Title.Key.TEXT_SIZE_1, Sp.Title.Default.TEXT_SIZE_1)),
                        Integer.parseInt(getValue(Sp.Title.Key.TEXT_SIZE_2, Sp.Title.Default.TEXT_SIZE_2)),
                        Integer.parseInt(getValue(Sp.Title.Key.TEXT_SIZE_3, Sp.Title.Default.TEXT_SIZE_3)),
                        Integer.parseInt(getValue(Sp.Title.Key.TEXT_SIZE_4, Sp.Title.Default.TEXT_SIZE_4)),
                        Integer.parseInt(getValue(Sp.Title.Key.TEXT_SIZE_5, Sp.Title.Default.TEXT_SIZE_5))
                )
        );

        // 分隔符
        setSeparator(
                new MdStyleSeparator(
                        Color.parseColor(getValue(Sp.Separator.Key.COLOR, Sp.Separator.Default.COLOR)),
                        Integer.parseInt(getValue(Sp.Separator.Key.SIZE, Sp.Separator.Default.SIZE))
                )
        );

        // 代码
        setCode(
                new MdStyleCode(
                        Color.parseColor(getValue(Sp.Code.Key.BACKGROUND_COLOR, Sp.Code.Default.BACKGROUND_COLOR)),
                        Color.parseColor(getValue(Sp.Code.Key.TEXT_COLOR, Sp.Code.Default.TEXT_COLOR)),
                        Integer.parseInt(getValue(Sp.Code.Key.TEXT_SIZE, Sp.Code.Default.TEXT_SIZE))
                )
        );

        // 粗斜体
        setBoldItalics(
                new MdStyleBoldItalics(
                        Color.parseColor(getValue(Sp.BoldItalics.Key.TEXT_COLOR, Sp.BoldItalics.Default.TEXT_COLOR)),
                        Integer.parseInt(getValue(Sp.BoldItalics.Key.TEXT_SIZE, Sp.BoldItalics.Default.TEXT_SIZE))
                )
        );

        // 粗体
        setBold(
                new MdStyleBold(
                        Color.parseColor(getValue(Sp.Bold.Key.TEXT_COLOR, Sp.Bold.Default.TEXT_COLOR)),
                        Integer.parseInt(getValue(Sp.Bold.Key.TEXT_SIZE, Sp.Bold.Default.TEXT_SIZE))
                )
        );

        // 斜体
        setItalics(
                new MdStyleItalics(
                        Color.parseColor(getValue(Sp.Italics.Key.TEXT_COLOR, Sp.Italics.Default.TEXT_COLOR)),
                        Integer.parseInt(getValue(Sp.Italics.Key.TEXT_SIZE, Sp.Italics.Default.TEXT_SIZE))
                )
        );
    }

    private String getValue(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    private interface Sp {
        interface Normal {
            interface Key {
                String TEXT_COLOR = "key_normal_text_color";
                String TEXT_SIZE = "key_normal_text_size";
            }

            interface Default {
                String TEXT_COLOR = "#FF000000";
                String TEXT_SIZE = "64";
            }
        }

        interface Quote {
            interface Key {
                String STRIPE_COLOR = "key_quote_stripe_color";
                String STRIPE_WIDTH = "key_quote_stripe_width";
                String GAP_WIDTH = "key_quote_gap_width";
                String TEXT_COLOR = "key_quote_text_color";
                String TEXT_SIZE = "key_quote_text_size";
            }

            interface Default {
                String STRIPE_COLOR = "#30888888";
                String STRIPE_WIDTH = "20";
                String GAP_WIDTH = "30";
                String TEXT_COLOR = "#A0000000";
                String TEXT_SIZE = "64";
            }
        }

        interface CodeBlock {
            interface Key {
                String GAP_WIDTH = "key_code_block_gap_width";
                String BACKGROUND_COLOR = "key_code_block_background_color";
                String TEXT_COLOR = "key_code_block_text_color";
                String TEXT_SIZE = "key_code_block_text_size";
            }

            interface Default {
                String GAP_WIDTH = "30";
                String BACKGROUND_COLOR = "#30888888";
                String TEXT_COLOR = "#A0000000";
                String TEXT_SIZE = "64";
            }
        }

        interface OrderedList {
            interface Key {
                String INDEX_COLOR = "key_ordered_list_index_color";
                String INDEX_SIZE = "key_ordered_list_index_size";
                String INDEX_WIDTH = "key_ordered_list_index_width";
                String GAP_WIDTH = "key_ordered_list_gap_width";
                String TEXT_COLOR = "key_ordered_list_text_color";
                String TEXT_SIZE = "key_ordered_list_text_size";
            }

            interface Default {
                String INDEX_COLOR = "#FF000000";
                String INDEX_SIZE = "56";
                String INDEX_WIDTH = "30";
                String GAP_WIDTH = "30";
                String TEXT_COLOR = "#FF000000";
                String TEXT_SIZE = "64";
            }
        }

        interface UnorderedList {
            interface Key {
                String CIRCLE_COLOR = "key_unordered_list_circle_color";
                String CIRCLE_RADIUS = "key_unordered_list_circle_radius";
                String GAP_WIDTH = "key_unordered_list_gap_width";
                String TEXT_COLOR = "key_unordered_list_text_color";
                String TEXT_SIZE = "key_unordered_list_text_size";
            }

            interface Default {
                String CIRCLE_COLOR = "#FF000000";
                String CIRCLE_RADIUS = "8";
                String GAP_WIDTH = "30";
                String TEXT_COLOR = "#FF000000";
                String TEXT_SIZE = "64";
            }
        }

        interface Title {
            interface Key {
                String TEXT_COLOR = "key_title_text_color";
                String TEXT_SIZE_1 = "key_title_text_size_1";
                String TEXT_SIZE_2 = "key_title_text_size_2";
                String TEXT_SIZE_3 = "key_title_text_size_3";
                String TEXT_SIZE_4 = "key_title_text_size_4";
                String TEXT_SIZE_5 = "key_title_text_size_5";
            }

            interface Default {
                String TEXT_COLOR = "#FF000000";
                String TEXT_SIZE_1 = String.valueOf(64 + 16 * 4);
                String TEXT_SIZE_2 = String.valueOf(64 + 16 * 3);
                String TEXT_SIZE_3 = String.valueOf(64 + 16 * 2);
                String TEXT_SIZE_4 = String.valueOf(64 + 16);
                String TEXT_SIZE_5 = String.valueOf(64);
            }
        }

        interface Separator {
            interface Key {
                String COLOR = "key_separator_color";
                String SIZE = "key_separator_size";
            }

            interface Default {
                String COLOR = "#30888888";
                String SIZE = "2";
            }
        }

        interface Code {
            interface Key {
                String BACKGROUND_COLOR = "key_code_background_color";
                String TEXT_COLOR = "key_code_text_color";
                String TEXT_SIZE = "key_code_text_size";
            }

            interface Default {
                String BACKGROUND_COLOR = "#10FF0000";
                String TEXT_COLOR = "#A0FF0000";
                String TEXT_SIZE = "64";
            }
        }

        interface BoldItalics {
            interface Key {
                String TEXT_COLOR = "key_bold_italics_text_color";
                String TEXT_SIZE = "key_bold_italics_text_size";
            }

            interface Default {
                String TEXT_COLOR = "#FF000000";
                String TEXT_SIZE = "64";
            }
        }

        interface Bold {
            interface Key {
                String TEXT_COLOR = "key_bold_text_color";
                String TEXT_SIZE = "key_bold_text_size";
            }

            interface Default {
                String TEXT_COLOR = "#FF000000";
                String TEXT_SIZE = "64";
            }
        }

        interface Italics {
            interface Key {
                String TEXT_COLOR = "key_italics_text_color";
                String TEXT_SIZE = "key_italics_text_size";
            }

            interface Default {
                String TEXT_COLOR = "#FF000000";
                String TEXT_SIZE = "64";
            }
        }
    }
}
