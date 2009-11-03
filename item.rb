class Item
  def initialize(d, t, r)
    @dir = d
    @title = t
    @rating = r
  end
  def dir  
    @dir  
  end  
  def title  
    @title  
  end  
  def rating  
    @rating
  end  
  def to_s
      "#{@dir}\t#{@title}\t#{@rating}"
  end 
end