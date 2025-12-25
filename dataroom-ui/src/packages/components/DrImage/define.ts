import { ChartDefine } from '@/packages/components/type/define.ts'
import thumbnail from './images/图片.png'

export default class DrImagePlugin extends ChartDefine {
  constructor(tags: string[]) {
    super('DrImage', '图片', thumbnail, tags)
  }
}
