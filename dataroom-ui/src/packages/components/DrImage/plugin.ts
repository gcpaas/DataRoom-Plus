import { ChartPlugin } from '@/packages/components/type/define.ts'
import thumbnail from './images/图片.png'
import { DrConst } from '@/packages/_common/_constant.ts'


export class DrImagePlugin extends ChartPlugin {
  constructor(tags: string[]) {
    super(DrConst.THIS_PLUGIN_TYPE, '图片', '图片,素材', thumbnail, tags)
  }
}
