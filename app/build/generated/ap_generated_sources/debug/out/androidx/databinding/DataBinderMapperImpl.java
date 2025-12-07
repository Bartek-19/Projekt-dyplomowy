package androidx.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new pl.pollub.android.powerstrongapp.DataBinderMapperImpl());
  }
}
